使ってるアプリが落ちた時に悪態をつくためのコマンド ochita.scala
======================
[@k_nishijima](https://twitter.com/k_nishijima)
2012年9月29日に沖縄を襲った台風に合わせて開催された、[台風17号ボッチソン](http://atnd.org/events/32696) = [#台風そん](https://twitter.com/i/#!/search/?q=%23%E5%8F%B0%E9%A2%A8%E3%81%9D%E3%82%93&src=typd) の成果です。  
アプリ名を指定してコマンドを叩くと、twitterで#ochitaを付けて悪態をつきます。　

事前準備
------
### twitterでアプリ作成 ###
[https://dev.twitter.com/](https://dev.twitter.com/)にてご自分のアプリを作成し、src/main/scala/ochita.scala の先頭付近にある consumerKey, consumerSecret, accessToken, accessTokenSecret を取得の上、各変数に設定しておいてください。

### ソースを展開し、修正した上でsbtでビルド ###
    $ mkdir ~/bin/ochita.scala/
	$ cd ~/bin/ochita.scala/
	$ 
    $ sbt
	> compile
	> assembly
    依存ライブラリとまとめて target/ochita-assembly-1.0.jar にアセンブリされます。

使い方
------
### コマンド引数 ###
    scala -Dfile.encoding=UTF-8 -cp ochita-assembly-1.0.jar Ochita YOUR-APPNAME [data store dir]

*   `YOUR-APPNAME` :
    落ちたアプリ名を英文字で入れてください

*   `[data store dir]` :
    履歴を保存するディレクトリを指定します。指定しない場合は、System.getProperty("java.io.tmpdir")に保存します。


### 適当にシェルスクリプトを作ってパスの通った場所においておきます ###
    $ cat ochita.sh

    /usr/bin/java -Dfile.encoding=UTF-8 -cp ~/bin/ochita.scala/target/ochita-assembly-1.0.jar Ochita $1 ~/bin/ochita.scala/var

実行
--------
    $ ./ochita.sh eclipse
    [dataFile = /Users/west/bin/ochita.scala/var/ochita]
    なんだと... eclipseがストライキに入りました 本日6回目/通算6回 #ochita


ライセンス
----------
build.sbtに指定されている各種ライブラリは、それぞれのライセンスになります。

Copyright &copy; 2012 Koichiro Nishijima
Licensed under the [Apache License, Version 2.0][Apache]
 
[Apache]: http://www.apache.org/licenses/LICENSE-2.0
