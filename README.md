[github]: https://github.com/Puotek/arma-config-plugin

[github:contributors]: https://github.com/Puotek/arma-config-plugin/graphs/contributors

[github:changelog]: https://github.com/Puotek/arma-config-plugin/blob/main/CHANGELOG.md

[marketplace:page]: https://plugins.jetbrains.com/plugin/29234-arma-config-support

[marketplace:versions]: https://plugins.jetbrains.com/plugin/29234-arma-config-support/versions

[puotek:discord]: https://discord.com/users/291967371646599169

[puotek:github]: https://github.com/Puotek

<!--suppress HtmlDeprecatedAttribute -->
<div align="center">

[<img src="https://capsule-render.vercel.app/api?type=waving&height=222&color=0:2046fb,15:7442ec,60:fd3a53,100:fd442a&text=Arma%20Config%20IntelliJ%20Plugin&textBg=false&fontAlignY=30&fontSize=55&animation=fadeIn&reversal=false&section=header&fontColor=FFFFFF&desc=by%20Puotek&descAlign=50&descAlignY=53"  alt="Arma 3 Config IntelliJ Plugin by Puotek"/>][marketplace:page]

[![Marketplace](https://img.shields.io/badge/Marketplace-2046fb?logo=jetbrains&logoColor=white)
][marketplace:page]
[![Version](https://img.shields.io/jetbrains/plugin/v/29234-arma-config-support.svg?label=Version&color=2046fb)
][marketplace:versions]
[![Downloads](https://img.shields.io/jetbrains/plugin/d/29234-arma-config-support.svg?label=Downloads&color=fd3a53)
][marketplace:versions]
[![Discord](https://img.shields.io/badge/Puotek-%235865F2.svg?logo=discord&logoColor=white)
][puotek:discord]

[<img src="https://contrib.rocks/image?repo=Puotek/arma-config-plugin"  alt="Contributor Profile Pictures in a grid"/>][puotek:github]

</div>

<!-- Plugin description -->

Language support for [Arma 3 config files](https://community.bistudio.com/wiki/Config.cpp/bin_File_Format) in JetBrains IntelliJ-based IDEs

### Features

- Syntax highlighting with color settings
- Breadcrumbs and sticky lines
- Inspections
- Support for macro usage in syntax checking
- Support for comment keybinds
- Working file structure window
- Formatting
- Folding
- [and more...][github:changelog]

<!-- Plugin description end -->

Designed it for myself, since I enjoy JetBrains IDEs, and the current existing arma plugin was old
and didn't support some of the nicer IntelliJ features like breadcrumbs and sticky lines.

## Installation

#### Using the IDE built-in plugin system

<kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "Arma Config Support"</kbd> >
<kbd>Install</kbd>

#### Using JetBrains Marketplace

Download the [latest release][marketplace:versions] from JetBrains Marketplace and install it manually using

<kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>

## Manual Version Build

To manually create a release: Open project in IntelliJ with the following plugins installed:

- [Plugin DevKit](https://plugins.jetbrains.com/plugin/22851-plugin-devkit)
- [Grammar-Kit](https://plugins.jetbrains.com/plugin/6606-grammar-kit)

Make sure to [set project JDK](https://www.jetbrains.com/help/idea/sdk.html#set-up-jdk) to version 21 (e.g. `Oracle OpenJDK 21.0.1`)

Run `buildDownloads` configuration or gradle task

Console might say error, but your plugin build should be in your `User/Downloads` folder. Install it manually using

<kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>
