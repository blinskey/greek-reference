# Ancient Greek Lexicon & Syntax

[![Build Status](https://travis-ci.org/blinskey/greek-reference.svg?branch=dev)](https://travis-ci.org/blinskey/greek-reference)

This is an ancient Greek lexicon and grammar for Android incorporating open source texts from the [Perseus Digital Library][Perseus]. It's also known by the alternate name Greek Reference.

<a href="https://play.google.com/store/apps/details?id=com.benlinskey.greekreference">
  <img alt="Get it on Google Play"
       src="https://developer.android.com/images/brand/en_generic_rgb_wo_60.png" />
</a>
<a href="http://www.amazon.com/gp/product/B00HV37XXG">
    <img alt="Available at Amazon" src="amazon-apps-store-us-black-177x60.png">
</a>

If you find this app useful, please consider [making a donation](GiveDirectly) to help poor households in Kenya and Uganda.

## Contents

- [Status](#status)
- [Building the App](#building-the-app)
- [Contributing](#contributing)
- [Frequently Asked Questions](#frequently-asked-questions)
- [Press](#press)
- [Third-Party Resources](#third-party-resources)
- [License](#license)

## Status

The latest release is version 1.9.0. The corresponding version of the [Greek Reference Database Creator][] is 1.2.0.

You can view a changelog for each release on the [Releases page][].

## Building the App

The texts used in this app are contained in a pair of databases called `lexicon.db` and `syntax.db`. These are generated using a simple Java program, the [Greek Reference Database Creator][], which is included as a submodule in this repository. The database files are zipped and placed in this project's `assets` directory. They must be named `lexicon.zip` and `syntax.zip` in order to work with the [SQLiteAssetHelper][Android SQLiteAssetHelper] library used to copy them to the user's device.

The app uses the new Gradle-based Android build system. If you'd like to assemble a signed APK, place your keystore in the root directory along with a `release.properties` file containing your keystore credentials. An example file called `release.properties.sample` is included in the project.

## Contributing

Pull requests are welcome and encouraged. Please read the [contributing guidelines][contributing] to get started.

## Frequently Asked Questions

**Can you add the full LSJ lexicon?**

The app only includes texts that the Perseus Digital Library has released under open source licenses. The full LSJ isn't available under such a license, so we're limited to the Middle Liddell. You can read more about this topic on the Perseus blog's ["Notes on the status of LSJ and Lewis and Short"](http://sites.tufts.edu/perseusupdates/other-information/notes-on-the-status-of-lsj-and-lewis-and-short/) page.

If you're using the app on a device connected to the Internet, you can use the View on Perseus option in the action bar menu to open the Perseus Greek Word Study Tool page for a word. The page will be displayed with properly rendered Greek characters and will contain links to the full LSJ entry.

**Can you translate the app to my language?**

Sorry, we're limited to texts available from the Perseus Digital Library. If you're interested in translating the app's UI text to another language, feel free to open an issue for further discussion.

**What is the official name of this app?**

The app was originally called Greek Reference but was later renamed to Ancient Greek Lexicon & Syntax in order to make it easier for users to find the app. At the moment, the original name still persists in this repository's name, the app icon caption, and a few other places.

**What should I do if I've spotted an error in the lexicon?**

Open an issue here or send an email to <greekreference@benlinskey.com>. We'll fix the text and pass the info on to Perseus.

**What should I do if I encounter a bug?**

If Android gives you the option to submit a crash report, please do so. You can also open an issue here or email <greekreference@benlinskey.com> with a description of the problem.

**I'd like to suggest a new feature or improvement to the app.**

Feel free to open an issue or send an email to <greekreference@benlinskey.com>. You can also check the [Issues](https://github.com/blinskey/greek-reference/issues) page for a list of features and bugfixes currently on our to-do list.

**Can you make a Latin version of the app?**

Several users have requested a Latin version. While I'm not planning to make such an app myself, I'd be happy to help anyone who wants to do so. Feel free to fork the project and to get in touch with me if you'd like to discuss the app's design.

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
