# Greek Reference: Ancient Greek Lexicon & Syntax

This is an ancient Greek lexicon and grammar for Android incorporating open
source texts from the [Perseus Digital Library][perseus].

The app can be downloaded for free from the [Google Play][] and [Amazon][] app
stores. Releases are also available in APK and AAB format on the [GitHub
releases page][releases].

Greek Reference was [recommended][nytimes] by the *New York Times Sunday Book
Review* in 2014 (referenced by its previous title, Ancient Greek Lexicon and
Syntax).

This app is no longer under development. I intend for version 1.16.0 to be the
final release. I'm grateful to everyone who has tried the app and offered
feedback over the years.

## Building the App

The texts used in this app are contained in a pair of databases called
`lexicon.db` and `syntax.db`. These are generated using a simple Java program,
the [Greek Reference Database Creator][], which is included as a submodule in
this repository. The database files are zipped and placed in this project's
`assets` directory. They must be named `lexicon.zip` and `syntax.zip` in order
to work with the [SQLiteAssetHelper][Android SQLiteAssetHelper] library used to
copy them to the user's device.

If you'd like to assemble a signed APK, place your keystore in the root
directory along with a `release.properties` file containing your keystore
credentials. An example file called `release.properties.sample` is included in
the project.

## Texts

This project includes two texts distributed by the [Perseus Digital
Library][perseus]:

- [*An Intermediate Greek-English Lexicon*][perseus-lexicon], by Henry George
  Liddell and Robert Scott
- [*Overview of Greek Syntax*][perseus-syntax], by Jeffrey A. Rydberg-Cox

These texts are licensed under a [Creative Commons Attribution-ShareAlike
3.0 United States license][CC BY-SA 3.0 US]. See the `LICENSE` file for details
regarding the texts' license and additional notices.

I am grateful to Jeffrey A. Rydberg-Cox for making his [*Overview of Greek
Syntax*][perseus-syntax] available through Perseus under a Creative Commons
license. You can visit his website at <http://daedalus.umkc.edu>.

## License

This project's source code is copyright 2013-2025 Benjamin Linskey and is
distributed under the Apache License, Version 2.0. See the `LICENSE` file for
a copy of the license and third-party license information.

[Greek Reference Database Creator]: https://github.com/blinskey/greek-reference-database-creator
[Apache]: http://www.apache.org/licenses/LICENSE-2.0
[Android SQLiteAssetHelper]: https://github.com/jgilfelt/android-sqlite-asset-helper
[Google Play]: https://play.google.com/store/apps/details?id=com.benlinskey.greekreference
[Amazon]: https://www.amazon.com/gp/product/B00HV37XXG
[releases]: https://github.com/blinskey/greek-reference/releases
[Android Action Bar Icon Pack]: http://developer.android.com/design/downloads/index.html
[CC BY-SA 3.0 US]: http://creativecommons.org/licenses/by-sa/3.0/us/
[nytimes]: http://www.nytimes.com/2014/08/24/books/review/gateways-to-the-classical-world.html
[perseus]: http://www.perseus.tufts.edu
[perseus-lexicon]: https://www.perseus.tufts.edu/hopper/text?doc=Perseus%3atext%3a1999.04.0058
[perseus-syntax]: https://www.perseus.tufts.edu/hopper/text?doc=Perseus%3atext%3a1999.04.0052
