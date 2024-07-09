# Greek Reference: Ancient Greek Lexicon & Syntax

<!-- [![Build Status](https://travis-ci.org/blinskey/greek-reference.svg?branch=dev)](https://travis-ci.org/blinskey/greek-reference) -->

This is an ancient Greek lexicon and grammar for Android incorporating open source texts from the [Perseus Digital Library][Perseus].

<a href="https://play.google.com/store/apps/details?id=com.benlinskey.greekreference">
  <img alt="Android app on Google Play" src="readme-img/en-play-badge.png" height="60">
</a>
<a href="http://www.amazon.com/gp/product/B00HV37XXG">
  <img alt="Available at Amazon" src="readme-img/amazon-apps-store-us-black-177x60.png">
</a>

Greek Reference is charityware. If you find this app useful, please consider making a donation to [GiveDirectly][GiveDirectly] or a charity of your choice.

Visit the [Greek Reference wiki][wiki] for frequently asked questions.

## Contents

- [Status](#status)
- [Building the App](#building-the-app)
- [Contributing](#contributing)
- [Press](#press)
- [Third-Party Resources](#third-party-resources)
- [License](#license)

## Status

Greek Reference is not under active development at this time.

You can view a changelog for each release on the [Releases page][].

## Building the App

The texts used in this app are contained in a pair of databases called `lexicon.db` and `syntax.db`. These are generated using a simple Java program, the [Greek Reference Database Creator][], which is included as a submodule in this repository. The database files are zipped and placed in this project's `assets` directory. They must be named `lexicon.zip` and `syntax.zip` in order to work with the [SQLiteAssetHelper][Android SQLiteAssetHelper] library used to copy them to the user's device.

The app uses the new Gradle-based Android build system. If you'd like to assemble a signed APK, place your keystore in the root directory along with a `release.properties` file containing your keystore credentials. An example file called `release.properties.sample` is included in the project.

## Contributing

Greek Reference is not under active development at this time. I am willing to consider pull requests but cannot guarantee a prompt response. Please review the [contributing guidelines][contributing] before sending a request..

## Press

- [The *New York Times Sunday Book Review*, "Gateways to the Classical World"](http://www.nytimes.com/2014/08/24/books/review/gateways-to-the-classical-world.html)

## Third-Party Resources

This program uses the following open source resources. Thanks to their creators for making their work available.

- [Android SQLiteAssetHelper][]
- [EpiDoc TransCoder][] (used in the [Greek Reference Database Creator][])
- [NotoSerif font][]
- [Perseus][] texts (see above)
- [TypefaceTextView][]
- [Android Developer Icons][android-icons]
- "Boilerplates" icons from <http://www.android-icons.com>
- [Google Material Design Icons][material-design-icons]
- [ScrimInsetsFrameLayout][] from the [Google I/O app][google-io]

I am particularly grateful to Jeffrey A. Rydberg-Cox for making his *Overview of Greek Syntax* available through Perseus under a Creative Commons license. You can visit his website at <http://daedalus.umkc.edu>.

### Texts

This project includes the text of *An Intermediate Greek-English Lexicon*, by Henry George Liddell and Robert Scott. Text provided by Perseus Digital Library, with funding from The Annenberg CPB/Project. Original version available for viewing and download at <http://www.perseus.tufts.edu/hopper/>. I have made a number of corrections to the original text.

The project also includes the text of *Overview of Greek Syntax*, by Jeffrey A. Rydberg-Cox. Text provided by Perseus Digital Library, with funding from The Annenberg CPB/Project. Original version available for viewing and download at <http://www.perseus.tufts.edu/hopper/>.

The above texts are licensed under a [Creative Commons Attribution-ShareAlike 3.0 United States license](CC BY-SA 3.0 US).

### Icons

The app icon was constructed in part from an icon obtained from the "Boilerplates" icon set formerly available at <http://www.android-icons.com>. The icon set is licensed under the [Creative Commons Attribution 3.0 Unported license][CC BY 3.0]. The app icon also includes an icon from the [Android Developer Icons][android-icons] set created by [Opoloo][]. That icon set is licensed under the [Creative Commons Attribution-ShareAlike 4.0 International license][CC BY-SA 4.0].

The remaining icons used in this app are taken from the [Google Material Design Icons][material-design-icons] set, licensed under a [Creative Common Attribution 4.0 International license][CC BY 4.0].

## License

This project's source code is licensed under the [Apache License, version 2.0][Apache]. The app icon is licensed under a [Creative Commons Attribution-Share Alike 4.0 International License][CC BY 4.0].

[Greek Reference Database Creator]: https://github.com/blinskey/greek-reference-database-creator
[Apache]: http://www.apache.org/licenses/LICENSE-2.0
[Android SQLiteAssetHelper]: https://github.com/jgilfelt/android-sqlite-asset-helper
[EpiDoc TransCoder]: http://sourceforge.net/projects/epidoc/files/Transcoder/
[NotoSerif font]: https://code.google.com/p/noto/
[Perseus]: http://www.perseus.tufts.edu
[TypefaceTextView]: http://www.tristanwaddington.com/2012/09/android-textview-with-custom-font-support/
[Google Play]: https://play.google.com/store/apps/details?id=com.benlinskey.greekreference
[Releases page]: https://github.com/blinskey/greek-reference/releases
[contributing]: CONTRIBUTING.md
[Android Action Bar Icon Pack]: http://developer.android.com/design/downloads/index.html
[GiveDirectly]: https://givedirectly.org
[CC By 3.0]: http://creativecommons.org/licenses/by/3.0/
[CC BY-SA 3.0 US]: http://creativecommons.org/licenses/by-sa/3.0/us/
[CC BY-SA 4.0]: http://creativecommons.org/licenses/by-sa/4.0/
[CC BY 4.0]: http://creativecommons.org/licenses/by/4.0/
[android-icons]: http://androidicons.com
[Opoloo]: http://www.opoloo.com/
[material-design-icons]: https://github.com/google/material-design-icons
[ScrimInsetsFrameLayout]: https://github.com/google/iosched/blob/master/android/src/main/java/com/google/samples/apps/iosched/ui/widget/ScrimInsetsFrameLayout.java
[google-io]: https://github.com/google/iosched
[wiki]: https://github.com/blinskey/greek-reference/wiki
