# Contributing

Thanks for your interest in contributing to Greek Reference! Please take a moment to review the following guidelines to make sure your code can be integrated into the project as smoothly as possible. Feel free to create an issue if you have any questions.

## Process

1. Visit the [issue tracker][] and either create a new issue or comment on an existing issue that you'd like to work on. If you're thinking about working on a non-trivial change or addition, it would be a good idea to discuss it here first to make sure that it's a good fit for the project.
2. Fork the repository.
3. Base your changes on the `dev` branch.
4. Build and test the app with your new code. (I'd like to eventually add a test suite to the project, but for now, please manually ensure that your changes don't break anything.)
5. Submit a pull request targeted to the `dev` branch.

## Git Workflow

Greek Reference uses a simple workflow: All development is based on the `dev` branch, and the `master` branch represents the latest release.
    
## Code Style

Please follow the Android Open Source Project [code style guidelines].

## Greek Reference Database Creator

The Greek Reference Database Creator is included in this project as a submodule, but if you'd like to make any changes to that code, it would be easiest to check out the [original repository][GRDBC] rather than attempting to work with the submodule. If you're thinking about making any non-trivial changes in that repository, it would be a good idea to first create an issue to discuss them, since the structure of the databases is fundamental to the app.

[issue tracker]: https://github.com/blinskey/greek-reference/issues?labels=todo&state=open
[code style guidelines]: http://source.android.com/source/code-style.html
[GRDBC]: https://github.com/blinskey/greek-reference-database-creator
