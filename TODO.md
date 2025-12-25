■ 新規タスク
・サブタスク
○ 進行中タスク
◑ 半分進行タスク
● レビュー中タスク
※ 開発ルールと注意点
✅ 完了タスク

■ 【調整】モーション/表情の簡素化とAI指示の明文化（RealtimeRobotHelper）
  - 目的: ジェスチャーを 666_BA_RArmS180 / 666_BA_RArmL180 / 666_BA_RArmR180 の3種類に限定し、表情は happy/think/sad のみに制限。AIへの使用方法を明記。
  - 実装:
    - BAモーションの可用性を起動時に非同期プローブ（motionPlay→短時間でstop）。
    - すべてのモーション呼び出しをBAの3種類にマッピング（明示/語義/フォールバック）。
    - `set_face_expression` を happy/think/sad のみに正規化。
    - セッションinstructionsに明確な使用ルールを追記（モーション/表情/ツール呼び出し）。
    - ツール定義の説明文を同趣旨に更新。
  - 確認: 実機で 666_BA_* の実在をログで確認（probe success/failure）。
  - 状態: ✅ 実装済み（要実機確認）

■ 【高優先】OpenAI Realtime モデル更新とシステム指示の強化
  - 目的: 最新リアルタイムモデルへの追従と、発話中に豊富なジェスチャーを行う厳密な日本語運用ポリシーの適用
  - 変更点（RealtimeRobotHelper）:
    - モデル: `gpt-4o-realtime-preview-2025-06-03` へ更新（`initializeSessionConfig` と WebSocket URL 両方）
    - システム指示: 日本語のみ・文単位で必ず `play_motion` を実行・内部API非表示・複数動作の組合せ・ニュートラルフォールバック等の詳細要件を全文適用
    - ツールスキーマ: `play_motion` の `parameters.required` を空配列に変更（モデル側の柔軟な呼び出しに合わせる）
  - 状態: ✅ 実装済み（ビルド確認待ち）

■ 【高優先】「Stop Conversation」ボタン押下後にロボットが自問自答を開始しモノローグがループする問題を解消
  - 原因調査: 会話停止フラグ / セッション終了処理 / 音声入力停止 / 出力ストリーム中断 / 応答生成タスクキャンセルの各状態遷移を確認
  - 対策案: 停止ボタンで音声入力・音声出力キュー・生成ストリームを確実にキャンセルし、内部トリガ（自動再開/継続処理）を無効化
  - 再発防止: 状態マシン（IDLE / LISTENING / RESPONDING / STOPPED）を導入し不正遷移でログ警告
  - 検証: 連続 Stop 押下 / 接続失敗後の Stop / 低回線環境 / エラー復帰後
  
  状態: ✅ 対応済み（実装・ビルド済み、要実機確認）
  変更点（RealtimeRobotHelper）:
  - 会話状態マシンを追加: `IDLE/LISTENING/RESPONDING/STOPPED`
  - `stopConversation()` を新設: `response.cancel` と `input_audio_buffer.clear` を送信し、音声再生/ロボット動作/TTS を停止
  - `stopVoiceInput()` の挙動を改善: 入力音声が無い場合は `response.create` を送らずバッファをクリア（空入力での自動応答を防止）
  - `startVoiceInput()` にガードを追加: 応答中/未接続時の開始を抑止、開始時に状態初期化
  - `response.created`/音声再生開始/完了/`response.done` で状態遷移を適正化（RESPONDING→IDLE など）
  - ループ原因となる自己発話キャプチャ抑止の強化: アシスタント発話中はマイクチャンク破棄し、完了後に解除

■ 【高優先】ロボット発話を全編日本語のみとする（英語等の混在排除）
  - システム/モデルプロンプトへ「常に日本語で回答」指示を明確化（英語指示より前に配置）
  - 応答生成後の言語検出（閾値判定）で日本語以外なら再生成 or 翻訳
  - Whisper 認識結果が英語の場合は日本語へ翻訳後にコンテキストへ格納
  - ログ: 入力言語 / 出力言語 / 再生成回数をデバッグ可能に

  状態: ✅ 実装済み（要実機確認）
  反映箇所: `RealtimeRobotHelper`
  - `initializeSessionConfig()`: 日本語固定のシステムプロンプトに差し替え（「必ず日本語だけで回答」等）
  - 軽量言語判定を追加（ひらがな/カタカナ/漢字の比率）
    - 入力: `response.audio_transcript.done` で入力言語をログ
    - 出力: `response.text.delta` を蓄積し、音声完了後に出力言語をログ
  - 備考: Realtime の音声はストリーミング再生のため、今回の段階では強制再生成は行わず、まずはプロンプト強化＋検出ログで運用確認

■ 【高優先】会話開始前に自動再生される「listen to song」ジェスチャを除去
  - 呼び出し箇所特定（初回接続 / Mic 開始 / セッション初期化完了コールバック）
  - 呼び出し削除またはフラグ（enableIntroGesture=false）化
  - 既存依存（初期姿勢リセット等）がある場合は軽量ノーアニメの代替導入
  - 回帰テスト: 初回接続→最初の発話までのレイテンシ / フリーズ有無

  状態: ✅ 対応済み（実装・ビルド済み、要実機確認）
  変更点:
  - `MainActivity#toggleVoiceInput` / `RealtimeActivity#toggleVoiceInput` での `666_LE_ListenSong` 自動再生を削除
  - `RealtimeRobotHelper#triggerResponseGesture` から `666_LE_ListenSong` を候補から除外（応答中に不自然な"聞き"ジェスチャ回避）
  - 明示的ツール呼び出し（"listen"）は維持し、AI側の意図的使用のみ許可

■ 【高優先】各応答で最低 2 つのジェスチャ（意味的＋バリエーション）を実行する仕様
  - 応答パイプライン: 音声/テキスト出力確定 → 感情/キーワード抽出 → ジェスチャ候補スコアリング → 2 件以上選択 → シーケンス再生（重複/競合防止）
  - 音声コマンド（明示指示）対応: 「手を挙げて」/「右手を挙げて」/「左手を挙げて」/「両手を挙げて」
    - マッピング: raise_hand / raise_right_hand / raise_left_hand / raise_both_hands（名称は実装定義を照合）
    - 明示指示は他ジェスチャより優先し、2 個目は補助感情ジェスチャ（例: nod / blink）
  - 自己認識的発話: 実行前に短い前置き（例: 「右手を挙げますね」）を TTS キュー先頭へインサート（重複抑制）
  - バリエーション戦略: 直近 N 回のジェスチャ履歴を保持し重複スコア減点
  - フォールバック: 候補不足時はニュートラル（idle_small）など安全ジェスチャ補充
  - 計測: 応答生成完了から最初のジェスチャ開始までの遅延 / シーケンス総時間

  状態: ✅ 対応済み（実装・ビルド済み、要実機確認）
  反映箇所: `RealtimeRobotHelper`
  - 新規: `triggerResponseGestures()` により、応答音声開始時に少なくとも2つのモーションを順次再生（約1.2秒間隔、非ブロッキング）
  - 明示指示パーサ: `parseExplicitGestureFromJapanese()` を追加（日本語のみ、正規化して検知）
    - 対応語: 右手/左手/両手 + 「挙げて/上げて/あげて/持ち上げて/…」, 「バンザイ/万歳/広げて」→ 両手（オープンアーム）
  - 意味的候補: ユーザー直近発話から簡易キーワード抽出（嬉/悲/考/挨拶/別れ/おじぎ/拍手/ダンス/写真など）
  - バリエーション: 直近5件の履歴で重複を抑制
  - フォールバック: nod / explain / openarm / clap / point(L/R) で補完

- UI から字幕（テキストデルトラ/トランスクリプト表示）を非表示化：`activity_main.xml` / `activity_realtime.xml` の `conversationText` を非表示、`MainActivity` / `RealtimeActivity` のテキスト更新コードを削除

---

● アプリ凍結/フリーズの可能性がある箇所（Android 9 実機）
・UIスレッドでのブロッキング動作：`motionPlay(..., true)` をメインスレッドで実行している可能性
  - 対象: `MainActivity` の初期ウェルカム/接続/エラー時、`RealtimeActivity` の接続/エラー時
  - 対応: すべて `motionPlay(..., false)` に変更し、完了は `onCompleteOfMotionPlay` コールバックで扱う

● Java 9+ API 使用による互換性問題：`Map.of(...)` の実行時クラッシュ/ハング懸念
・対象: `RealtimeRobotHelper#initializeSessionConfig` の `sessionConfig.put("input_audio_transcription", Map.of("model", "whisper-1"));`
  - 対応: 互換性のある `HashMap` に置き換え（例: `Map<String, Object> m = new HashMap<>(); m.put("model", "whisper-1");`）
  - 備考: coreLibraryDesugaring 未導入のため Android 9 で `Map.of` は未サポートの可能性が高い

● メインスレッドの短時間スリープ：UI停止の原因になり得る
・対象: `RealtimeRobotHelper#stopVoiceInput` の `Thread.sleep(100)`（UIから直呼び出しされるケースあり）
  - 対応: `Handler.postDelayed(...)` など非ブロッキング手段へ変更

○ ロボットウィンドウ操作のブロッキング懸念
・対象: `hideWindow(true)`/`showWindow(true)` を UI スレッドから呼ぶ箇所
  - 対応: 必要に応じてワーカースレッド化、もしくはコールバックで制御し UI スレッドを占有しない

○ 音声入出力の互換性と初期化コスト
・対象: `AudioRecord/AudioTrack` の 24kHz 固定（端末差で初期化失敗/リソース負荷になり得る）
  - 対応: サポート有無を検証しフォールバックサンプルレートを用意、初期化は必要時に遅延実行

○ 高頻度 UI 更新によるカクつき
・対象: `runOnUiThread` でのテキスト差分更新（デルタごとに更新）
  - 対応: バッファリング/スロットリングで更新頻度を抑制

○ 依存関係/ビルド設定の整合性
・対象: Java 11 言語レベル使用中だが、Java 9+ ライブラリ API 利用が混在
  - 対応: Java 9+ ライブラリ API の使用禁止、または `coreLibraryDesugaring` の導入を検討

○ 初期化処理の重さによる初回操作時の体感フリーズ

✅ 対応済み（コード修正）
- `MainActivity`/`RealtimeActivity`: すべての `motionPlay(..., true)` を `false` に変更（非ブロッキング化）
- `RealtimeRobotHelper#initializeSessionConfig`: `Map.of` を `HashMap` 実装に置換（Android 9 互換）
- `RealtimeRobotHelper#stopVoiceInput`: `Thread.sleep(100)` を UI ブロックしない `executorService` 経由遅延送信に変更

✅ ビルド/デプロイ
- `./gradlew :app:assembleDebug` 成功
- `./deploy.sh` で実機へ `adb install -r` 成功（ログ閲覧はスキップ）

● レビュー中タスク（動作確認）
- 実機で以下のフローを確認：起動→「Connect」→（接続失敗可）→「Mic」→一度目のクリック後にフリーズしないこと
- OpenAI キー未設定時でも UI が固まらず、エラーメッセージ表示・操作継続できること
・対象: `onWikiServiceStart` 直後に重い処理（ロボット動作や各種初期化）を UI スレッドで実行
  - 対応: 初回ジェスチャは非ブロッキング実行＋バックグラウンド初期化へ分離

■ メンテナンス: ログ出力の全体確認（プロダクション前クリーン）
  - 状態: ✅ 完了
  - 結果: コードベースに `android.util.Log`/`Log.*`/`System.out.println`/`printStackTrace`/`Timber.*` の使用は無し。
  - 補足: `DOCS.md` 内のコード例に `Log.d` が残存（ドキュメントのみ、アプリ動作へ影響なし）。

■ 仕様: 表情・モーションのみ最小ログを出力
  - 目的: 実行状況の把握（例: `888_ML_Openarm_11 success`）
  - 実装: `android.util.Log` を使用し、開始/成功/失敗を出力
    - モーション: `RealtimeRobotHelper#playMotion`, `triggerResponseGestures`, `MotionAdapter#playMotion`
    - 表情: `FaceExpressionManager#processExpression`（未知表情時は failure を出力）
  - 状態: ✅ 実装済み
  - 補助: 端末で色付き表示スクリプト追加（緑=success, 赤=failure, 黄=start）
    - 実行: `bash scripts/logs.sh`（任意: `bash scripts/logs.sh HelloKebbi`）

■ 一時対応: モーション全停止・表情のみ運用
  - 目的: ロボット動作を止め、顔表情のみで応答を確認
  - 実装:
    - `RealtimeRobotHelper#triggerResponseGestures`: 早期 return（自動ジェスチャ停止）
    - `RealtimeRobotHelper#playMotion`: 実行せず `disabled` をログ
    - 関連 Activity の `mRobot.motionPlay(...)` 呼び出しを無効化
    - `MotionAdapter`: 再生操作を無効化（トーストとログのみ）
  - 状態: ✅ 実装済み

■ 仕様: デフォルト顔を常時表示し、表情呼び出し時のみ一時変更
  - 実装: `FaceExpressionManager` 初期化時に `showWindow(true)` を実行し常時表示
  - 表情再生後は `showWindow(true)` を再度呼び出し、デフォルト顔へ復帰
  - 状態: ✅ 実装済み（継続表示・簡易復帰）

■ 調整: 口パク速度をやや高速化
  - 背景: SDK 仕様「値が小さいほど速い」。従来 `200L` → 体感遅め
  - 対応: `RealtimeRobotHelper#handleAudioDelta` の `mouthOn(200L)` を `mouthOn(120L)` に変更
  - 状態: ✅ 実装済み（更に調整可: 100L/80L 等）
■ 仕様: しゃべり過ぎ対策（簡潔モード）
  - システム指示を強化: 原則1〜2文、要点のみを明示
  - ハード制限: `RealtimeRobotHelper`
    - テキスト長: `maxResponseChars=160` 超で `response.cancel`
    - 音声長: `maxSpeechMillis=4500ms` 経過で `response.cancel` + 再生停止
    - 変更点: `requestCancelCurrentResponse()` を追加、`handleTextDelta`/`handleAudioDelta` から呼び出し
  - 調整API: `setMaxResponseChars(int)`, `setMaxSpeechMillis(int)` を公開
  - 状態: ✅ 実装済み（微調整可）
