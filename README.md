# Greek Reference

This is an ancient Greek lexicon and grammar for Android incorporating open source texts from the [Perseus Digital Library][Perseus].

<a href="https://play.google.com/store/apps/details?id=com.benlinskey.greekreference">
  <img alt="Get it on Google Play"
       src="https://developer.android.com/images/brand/en_generic_rgb_wo_60.png" />
</a>

## Status

The latest release is version 1.0.2. The corresponding version of the [Greek Reference Database Creator][] is 1.1.0. You can view a changelog for each release on the [Releases page][].

## Building the App

The texts used in this app are contained in a pair of databases called `lexicon.db` and `syntax.db`. These are generated using a simple Java program, the [Greek Reference Database Creator][]. They are then zipped and placed in this project's `assets` directory. The files must be named `lexicon.zip` and `syntax.zip` in order to work with the [SQLiteAssetHelper][Android SQLiteAssetHelper] library used to copy them to the user's device.

Greek Reference uses the new Gradle-based Android build system. The Gradle build file is set up to prompt the user for developer key information when building the project. If you'd just like to build a debug APK using Android Studio, make sure to comment out the `release` sections of the `GreekReference/build.gradle` file. Otherwise, Android Studio won't be able to build the project. You'll also need to copy your `debug.keystore` file from your `.android` directory to the `GreekReference` directory.

Unfortunately, I'm not able to distribute all of the icons used in this app. [See below](https://github.com/blinskey/greek-reference#icons) for details. For now, you'll need to provide your own properly named icons in order to build the project. I'd like to cobble together a set of placeholder icons at some point in the future to remedy this problem. (See issue #39.)

## Contributing

Pull requests are welcome and encouraged. Please target your requests to the `dev` branch. All development happens there and is merged into `master` to create release builds.

## Icons

This project includes icons from <http://www.androidicons.com>. Since they're not licensed for redistribution, the icons are excluded from the repository. If you'd like to build the app yourself, you'll need to provide your own replacement icons. Sorry for the inconvenience.

The app icon, which is also excluded from the repository, was constructed in part from an icon obtained from the "Boilerplates" icon set available at <http://www.android-icons.com>. The icon set is licensed under the Creative Commons Attribution 3.0 Unported License. See <http://creativecommons.org/licenses/by/3.0/> for details.

## Texts

This project includes the text of *An Intermediate Greek-English Lexicon*, by Henry George Liddell and Robert Scott. Text provided by Perseus Digital Library, with funding from The Annenberg CPB/Project. Original version available for viewing and download at <http://www.perseus.tufts.edu/hopper/>.

The project also includes the text of *Overview of Greek Syntax*, by Jeffrey A. Rydberg-Cox. Text provided by Perseus Digital Library, with funding from The Annenberg CPB/Project. Original version available for viewing and download at <http://www.perseus.tufts.edu/hopper/>.

The above texts are licensed under a Creative Commons Attribution-ShareAlike 3.0 United States License. See <http://creativecommons.org/licenses/by-sa/3.0/us/> for details.

## Third-Party Resources

This program uses the following open-source resources. Thanks to their creators for making their work available.

- [Android SQLiteAssetHelper][]
- [EpiDoc TransCoder][] (used in the [Greek Reference Database Creator][])
- [NotoSerif font][]
- [Perseus][] texts (see above)
- [TypefaceTextView][]
- "Boilerplates" icons from <http://www.android-icons.com>

I am particularly grateful to Jeffrey A. Rydberg-Cox for making his *Overview of Greek Syntax* available through Perseus under a Creative Commons license. You can visit his website at <http://daedalus.umkc.edu>.

## License

This project is licensed under the [Apache License, version 2.0][Apache], with the exception of the texts of *An Intermediate Greek-English Lexicon* and *Overview of Greek Syntax*, which are distributed under Creative Commons licenses as [described above](https://github.com/blinskey/greek-reference#texts).

[Greek Reference Database Creator]: https://github.com/blinskey/greek-reference-database-creator
[Apache]: http://www.apache.org/licenses/LICENSE-2.0
[Android SQLiteAssetHelper]: https://github.com/jgilfelt/android-sqlite-asset-helper
[EpiDoc TransCoder]: http://sourceforge.net/projects/epidoc/files/Transcoder/
[NotoSerif font]: https://code.google.com/p/noto/
[Perseus]: http://www.perseus.tufts.edu
[TypefaceTextView]: http://www.tristanwaddington.com/2012/09/android-textview-with-custom-font-support/
[Google Play]: https://play.google.com/store/apps/details?id=com.benlinskey.greekreference
[Releases page]: https://github.com/blinskey/greek-reference/releases
