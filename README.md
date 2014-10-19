# Ancient Greek Lexicon & Syntax

This is an ancient Greek lexicon and grammar for Android incorporating open source texts from the [Perseus Digital Library][Perseus]. It's also known by the alternate name Greek Reference.

<a href="https://play.google.com/store/apps/details?id=com.benlinskey.greekreference">
  <img alt="Get it on Google Play"
       src="https://developer.android.com/images/brand/en_generic_rgb_wo_60.png" />
</a>
<a href="http://www.amazon.com/gp/product/B00HV37XXG">
    <img alt="Available at Amazon" src="amazon-apps-store-us-black-177x60.png">
</a>

## Contents

- [Status](#status)
- [Building the App](#building-the-app)
- [Contributing](#contributing)
- [Frequently Asked Questions](#frequently-asked-questions)
- [Press](#press)
- [Icons](#icons)
- [Texts](#texts)
- [Third-Party Resources](#third-party-resources)
- [License](#license)

## Status

The latest release is version 1.4.2. The corresponding version of the [Greek Reference Database Creator][] is 1.2.0.

You can view a changelog for each release on the [Releases page][].


## Building the App

The texts used in this app are contained in a pair of databases called `lexicon.db` and `syntax.db`. These are generated using a simple Java program, the [Greek Reference Database Creator][], which is included as a submodule in this repository. The database files are zipped and placed in this project's `assets` directory. They must be named `lexicon.zip` and `syntax.zip` in order to work with the [SQLiteAssetHelper][Android SQLiteAssetHelper] library used to copy them to the user's device.

The app uses the new Gradle-based Android build system. If you'd like to assemble a signed APK, place your keystore in the root directory along with a `release.properties` file containing your keystore credentials. An example file called `release.properties.sample` is included in the project.

Unfortunately, some of the icons used in the published version of this app are not licensed for redistribution and are therefore excluded from the repository. [See below](https://github.com/blinskey/greek-reference#icons) for details. The `GreekReference/src/res/placeholder-icons` directory contains a collection of icons from the free [Android Action Bar Icon Pack][] that can be used in place of the non-free icons used in the published version of this app. They're just white rectangles, intended to make it easy to build the app, not to be functional. To use them, copy the contents of this directory into `res`, merging the identically named `drawable` directories. If you'd like to suggest some more suitable freely licensed icons, please feel free to submit a pull request.

## Contributing

Pull requests are welcome and encouraged. Please read the [contributing guidelines][contributing] to get started.

## Frequently Asked Questions

**Can you add the full LSJ lexicon?**

The app only includes texts that the Perseus Digital Library has released under open source licenses. The full LSJ isn't available under such a license, so we're limited to the LSJ. You can read more about this topic on the Perseus blog's ["Notes on the status of LSJ and Lewis and Short"](http://sites.tufts.edu/perseusupdates/other-information/notes-on-the-status-of-lsj-and-lewis-and-short/) page. 

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

## Icons

This project includes icons from <http://www.androidicons.com>.

The app icon, which is excluded from the repository, was constructed in part from an icon obtained from the "Boilerplates" icon set available at <http://www.android-icons.com>. The icon set is licensed under the Creative Commons Attribution 3.0 Unported License. See <http://creativecommons.org/licenses/by/3.0/> for details.

## Texts

This project includes the text of *An Intermediate Greek-English Lexicon*, by Henry George Liddell and Robert Scott. Text provided by Perseus Digital Library, with funding from The Annenberg CPB/Project. Original version available for viewing and download at <http://www.perseus.tufts.edu/hopper/>. I have made a number of corrections to the original text.

The project also includes the text of *Overview of Greek Syntax*, by Jeffrey A. Rydberg-Cox. Text provided by Perseus Digital Library, with funding from The Annenberg CPB/Project. Original version available for viewing and download at <http://www.perseus.tufts.edu/hopper/>.

The above texts are licensed under a Creative Commons Attribution-ShareAlike 3.0 United States License. See <http://creativecommons.org/licenses/by-sa/3.0/us/> for details.

## Third-Party Resources

This program uses the following open source resources. Thanks to their creators for making their work available.

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
[contributing]: CONTRIBUTING.md
[Android Action Bar Icon Pack]: http://developer.android.com/design/downloads/index.html
