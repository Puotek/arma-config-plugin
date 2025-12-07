[template]: https://github.com/JetBrains/intellij-platform-plugin-template

[github]: https://github.com/Puotek/arma-config-plugin

[marketplace:page]: https://plugins.jetbrains.com/plugin/29234-arma-config-support

[marketplace:versions]: https://plugins.jetbrains.com/plugin/29234-arma-config-support/versions

[discord:puotek]: https://discord.com/users/291967371646599169

<!--suppress HtmlDeprecatedAttribute -->
<div align="center">

<img src="https://capsule-render.vercel.app/api?type=waving&height=222&color=0:2046fb,15:7442ec,60:fd3a53,100:fd442a&text=Arma%20Config%20IntelliJ%20Plugin&textBg=false&fontAlignY=30&fontSize=55&animation=fadeIn&reversal=false&section=header&fontColor=FFFFFF&desc=by%20Puotek&descAlign=50&descAlignY=53"  alt="Arma 3 Config IntelliJ Plugin by Puotek"/>

[![IntelliJ IDEA](https://img.shields.io/badge/-black?logo=intellij-idea&logoColor=white)
][marketplace:page]
[![JetBrains](https://img.shields.io/badge/Marketplace-2046fb?logo=jetbrains&logoColor=white)
][marketplace:page]
[![Version](https://img.shields.io/jetbrains/plugin/v/29234-arma-config-support.svg?label=Version&color=2046fb)
][marketplace:versions]
[![Downloads](https://img.shields.io/jetbrains/plugin/d/29234-arma-config-support.svg?label=Downloads&color=fd3a53)
][marketplace:page]
[![Discord](https://img.shields.io/badge/Puotek-%235865F2.svg?logo=discord&logoColor=white)
][discord:puotek]


<img src="https://contrib.rocks/image?repo=Puotek/arma-config-plugin"  alt="Contributor Profile Pictures in a grid"/>

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

<!-- Plugin description end -->

Designed it for myself, since I enjoy JetBrains IDEs, and the current existing arma plugin was old
and didn't support some of the nicer IntelliJ features like breadcrumbs and sticky lines.

## Installation

#### Using the IDE built-in plugin system

<kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "
arma-config-plugin"</kbd> >
<kbd>Install</kbd>

#### Using JetBrains Marketplace

Download the [latest release][marketplace:versions] from JetBrains Marketplace and install it manually using

<kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>

## Manual Build

Open project in IntelliJ with the following plugins installed:

- [Plugin DevKit](https://plugins.jetbrains.com/plugin/22851-plugin-devkit)
- [Grammar-Kit](https://plugins.jetbrains.com/plugin/6606-grammar-kit)

Make sure to [set project JDK](https://www.jetbrains.com/help/idea/sdk.html#set-up-jdk) to version 21 (e.g. `Oracle OpenJDK 21.0.1`)

[<kbd>src/main/grammar/Cfg.bnf</kbd>](src/main/grammar/Cfg.bnf) > <kbd>RMB</kbd> > <kbd>Generate Parser Code</kbd>

Run `buildDownloads` configuration or gradle task

Console might say error, but your plugin build should be in your `User/Downloads` folder

Install it manually using

<kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>
