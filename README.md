
# What is this?

LWJGLのサンプルコードを試した
https://www.lwjgl.org/guide

サンプルをベースにテクスチャーを描画し、操作するところまで簡易的に実装した。

# Why made?

LWJGLを使ってみたかった。
Ubuntuマシンにセットアップして動作させたかった。
無事動いた。

# Setup

普段はVimで開発しているので、build.shとrun.shで実行までを行っている。  

1. 好きな場所にJava用のライブラリを置いておく場所を作る
  - 例: ~/java/lib
2. `$JAVA_LIB_PATH` にそのパスを設定する（.bashrcなどに書き込んでおくと楽）
  - 例: `export JAVA_LIB_PATH=~/java/lib`
3. その中にLWJGLの公式からダウンロードして解凍したファイルをそのまま設置する
  - `$JAVA_LIB_PATH/lwjgl/jar/lwjgl.jar` でアクセスできるようになっていればOK
4. `./build.sh && ./run.sh` でビルド＆実行する


# Environment

## Java

Oracle-Java

```
$ java -version

java version "1.8.0_101"
Java(TM) SE Runtime Environment (build 1.8.0_101-b13)
Java HotSpot(TM) 64-Bit Server VM (build 25.101-b13, mixed mode)
```

OpenJDKではMinecraftが不具合を起こした経験から、Oracleにした。

## LWJGL
LWJGL 3.0.0 build 90

