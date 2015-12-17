# Contributing #
  1. Install the [Android Development Tools](http://developer.android.com/sdk/eclipse-adt.html) plugin for Eclipse.
  1. Get the WebViewGM library from [github](https://github.com/wbayer/webview-gm). If you are planning on improving this library, you will need a github account (see the readme on contributing there).
  1. Import the WebViewGM source as new Eclipse project "webview-gm".
  1. Get the Pardus Android source and import it as new Eclipse project "pardus". Make sure the project uses webview-gm as library.
  1. Make your changes. Contact me to get added as project committer. Commit your changes using Subversion (and your Google account).


# About Pardus for Android #
Pardus for Android, available on [Android Market](https://market.android.com/details?id=at.pardus.android.browser), is a convenient web client for the [massive multiplayer online game Pardus](http://www.pardus.at/). Besides the standard browser component its features are:

## Local images ##
An image pack is downloaded at the application's first start. Subsequently images are loaded only from the local image pack, saving expensive mobile network traffic and improving performance.

## Extensibility ##
Support of Greasemonkey-compatible user scripts make Pardus fully customizable on your Android phone.

## Secure access ##
By default all data from and to the Pardus servers is securely sent through HTTPS. Links unrelated to Pardus are opened in a new browser application without access to sensitive data. Optionally you can set the app to log your account out of Pardus whenever it loses focus.

## Fast access ##
If you choose to save your account credentials, you are automatically logged in at the app's startup. A floating menu bar instead of two frames saves space, and the customizable links let you browse through pages more quickly.


# About Pardus #
[Pardus](http://www.pardus.at/index.php?section=about) is a FREE award-winning, real-time graphical game set in a futuristic universe that can be played right from your browser.