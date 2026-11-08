# Messages Control

- [English](#english)
  - [Features](#features)
  - [Requirements](#requirements)
  - [Installation](#installation)
  - [Configuration](#configuration)
  - [Message variables](#message-variables)
  - [Color codes](#color-codes)
  - [Aliases](#aliases)
  - [Example](#example)
  - [Contributing](#contributing)
  - [Security](#security)
  - [License](#license)
- [Français](#français)
  - [Fonctionnalités](#fonctionnalités)
  - [Prérequis](#prérequis)
  - [Installer le mod](#installer-le-mod)
  - [Configurer le mod](#configurer-le-mod)
  - [Variables des messages](#variables-des-messages)
  - [Codes couleur](#codes-couleur)
  - [Alias](#alias)
  - [Exemple](#exemple)
  - [Contribuer](#contribuer)
  - [Sécurité](#sécurité)
  - [Licence](#licence)

---

## English

A NeoForge mod that lets server admins show, hide or rewrite the messages Minecraft sends to the chat: joins, leaves, advancements and deaths.

### Features

- **Join and leave messages:** keep the vanilla text, hide it, or write your own.
- **First join message:** greet players the first time they join the server.
- **Advancement messages:** hide them by type (task, goal, challenge), by namespace or by exact id, with a whitelist for exceptions. Players still get the advancement and its toast.
- **Death messages:** keep, hide or rewrite them, with the dimension and coordinates available. The death screen does not change.
- **Aliases:** replace technical values such as `minecraft:the_nether` with your own text.
- **Console logging:** hidden messages can still be written to the server console for moderation.

### Requirements

| Minecraft | NeoForge | Java | Config file |
|---|---|---|---|
| 26.3 | 26.3.0.76 or newer | 25 | `config/messagescontrol-synced.toml` |
| 26.1.2 to 26.2 | 26.1.2.126 or newer | 25 | `config/messagescontrol-server.toml` |
| 1.21.11 | 21.11.42 or newer | 21 | `config/messagescontrol-server.toml` |

Each row has its own jar: a jar only works on the Minecraft versions of its row.

### Installation

1. Download the jar for your Minecraft version from [Modrinth](https://modrinth.com/project/messages-control).
2. Put it in your server's `mods` folder.
3. Start the server once to generate the config file listed in [Requirements](#requirements).

When you update to Minecraft 26.3, copy your settings from `messagescontrol-server.toml` to `messagescontrol-synced.toml`.

The mod only changes messages on the server, so players don't need to install it.

### Configuration

Every message type except the first join message keeps its vanilla behavior until you change it.

| Setting | Default | Description |
|---|---|---|
| `logToConsole` | `true` | Writes hidden messages to the server console. |
| `join.enabled`, `leave.enabled`, `advancements.enabled`, `death.enabled` | `true` | `false` hides this type of message. |
| `join.mode`, `leave.mode`, `advancements.mode`, `death.mode` | `VANILLA` | `VANILLA` keeps the Minecraft message, `CUSTOM` uses `customMessage`. |
| `firstJoin.enabled` | `false` | `true` shows `firstJoin.customMessage` on a first join, even if `join.enabled` is `false`. `false` uses the regular `[join]` settings. |
| `customMessage` (in each section) | English text | The text to display. See [variables](#message-variables) and [colors](#color-codes). |

Advancement filters are in `[advancements.filters]`:

| Setting | Default | Description |
|---|---|---|
| `hideTasks`, `hideGoals`, `hideChallenges` | `false` | Hides every advancement of this type. |
| `hiddenNamespaces` | `[]` | Hides whole namespaces, for example `["create"]`. |
| `hiddenAdvancements` | `[]` | Hides exact advancements, for example `["minecraft:story/mine_stone"]`. |
| `whitelist` | `[]` | Ids or namespaces that are always shown, whatever the filters above say. |

`advancements.enabled = false` hides every advancement message, including whitelisted ones.

### Message variables

| Section | Variable | Value |
|---|---|---|
| `join`, `firstJoin`, `leave` | `{player}` | Username |
| | `{display_name}` | Display name, including any nickname or team prefix |
| | `{online}`, `{max}` | Players online, maximum players |
| `advancements` | `{player}` | Username |
| | `{advancement}` | Translated title, with the vanilla hover text |
| | `{advancement_id}` | Id, for example `minecraft:story/mine_stone` |
| | `{type}` | `task`, `goal` or `challenge` |
| `death` | `{player}` | Username |
| | `{death_message}` | Vanilla death message, translated |
| | `{dimension}` | Id, for example `minecraft:the_nether` |
| | `{x}`, `{y}`, `{z}` | Position of the death |

A variable that doesn't exist for the section is displayed as written.

### Color codes

Use the standard Minecraft codes with `&`:

- **Colors:** `&0` to `&9`, `&a` to `&f`
- **Formats:** `&k` obfuscated, `&l` bold, `&m` strikethrough, `&n` underline, `&o` italic
- **Reset:** `&r`

A color cancels the formats before it, so write the color first: `&e&l`, not `&l&e`.

A color placed before `{advancement}` replaces its vanilla color. Put `&r` before it to keep the vanilla green, or purple for challenges.

### Aliases

The `[aliases]` section replaces the values of `{dimension}`, `{type}` and `{advancement_id}`, and only those.

- The `minecraft:` prefix is optional. Ids from other mods must be written in full.
- Keys containing `:` or `/` need quotes.
- Color codes don't work in an alias. Put the color in the message, before the variable.

### Example

```toml
logToConsole = true

[join]
	enabled = true
	mode = "CUSTOM"
	customMessage = "&a+ &f{display_name} &7({online}/{max})"

[firstJoin]
	enabled = true
	customMessage = "&6Welcome to {display_name}, who just joined for the first time!"

[leave]
	enabled = false

[advancements]
	enabled = true
	mode = "VANILLA"

	[advancements.filters]
		hideTasks = true
		whitelist = ["minecraft:story/mine_diamond"]

[death]
	enabled = true
	mode = "CUSTOM"
	customMessage = "&c{death_message} &7in {dimension} ({x}, {y}, {z})"

[aliases]
	overworld = "the Overworld"
	the_nether = "the Nether"
	the_end = "the End"
	"mymod:my_dimension" = "my dimension"
```

### Contributing

Bug reports and ideas are welcome in the [issues](https://github.com/SacBruine237275/messages-control/issues). Before opening a pull request, read [CONTRIBUTING.md](CONTRIBUTING.md).

### Security

To report a vulnerability, see [SECURITY.md](SECURITY.md). Please don't open a public issue.

### License

This mod is released under the [MIT License](LICENSE).

This project is not affiliated with Mojang, Microsoft or NeoForged.

---

## Français

Un mod NeoForge qui permet aux administrateurs de serveur d'afficher, de masquer ou de réécrire les messages que Minecraft envoie dans le chat : connexions, déconnexions, progrès et morts.

### Fonctionnalités

- **Messages de connexion et de déconnexion :** garde le texte vanilla, masque-le ou écris le tien.
- **Message de première connexion :** accueille les joueurs la première fois qu'ils rejoignent le serveur.
- **Messages de progrès :** masque-les par type (task, goal, challenge), par namespace ou par id exact, avec une liste blanche pour les exceptions. Les joueurs obtiennent quand même le progrès et sa notification.
- **Messages de mort :** garde-les, masque-les ou réécris-les, avec la dimension et les coordonnées disponibles. L'écran de mort ne change pas.
- **Alias :** remplace les valeurs techniques comme `minecraft:the_nether` par ton propre texte.
- **Journal de la console :** les messages masqués peuvent quand même être écrits dans la console du serveur, pour la modération.

### Prérequis

| Minecraft | NeoForge | Java | Fichier de config |
|---|---|---|---|
| 26.3 | 26.3.0.76 ou plus récent | 25 | `config/messagescontrol-synced.toml` |
| 26.1.2 à 26.2 | 26.1.2.126 ou plus récent | 25 | `config/messagescontrol-server.toml` |
| 1.21.11 | 21.11.42 ou plus récent | 21 | `config/messagescontrol-server.toml` |

Chaque ligne a son propre jar : un jar ne fonctionne que sur les versions de Minecraft de sa ligne.

### Installer le mod

1. Télécharge le jar de ta version de Minecraft sur [Modrinth](https://modrinth.com/project/messages-control).
2. Place-le dans le dossier `mods` de ton serveur.
3. Démarre le serveur une fois pour générer le fichier de config indiqué dans les [prérequis](#prérequis).

Quand tu passes à Minecraft 26.3, copie tes réglages de `messagescontrol-server.toml` vers `messagescontrol-synced.toml`.

Le mod ne modifie les messages que côté serveur, les joueurs n'ont donc pas besoin de l'installer.

### Configurer le mod

Chaque type de message, sauf le message de première connexion, garde son comportement vanilla tant que tu ne le changes pas.

| Option | Défaut | Description |
|---|---|---|
| `logToConsole` | `true` | Écrit les messages masqués dans la console du serveur. |
| `join.enabled`, `leave.enabled`, `advancements.enabled`, `death.enabled` | `true` | `false` masque ce type de message. |
| `join.mode`, `leave.mode`, `advancements.mode`, `death.mode` | `VANILLA` | `VANILLA` garde le message de Minecraft, `CUSTOM` utilise `customMessage`. |
| `firstJoin.enabled` | `false` | `true` affiche `firstJoin.customMessage` à la première connexion, même si `join.enabled` vaut `false`. `false` utilise les réglages normaux de `[join]`. |
| `customMessage` (dans chaque section) | Texte en anglais | Le texte à afficher. Voir les [variables](#variables-des-messages) et les [couleurs](#codes-couleur). |

Les filtres de progrès sont dans `[advancements.filters]` :

| Option | Défaut | Description |
|---|---|---|
| `hideTasks`, `hideGoals`, `hideChallenges` | `false` | Masque tous les progrès de ce type. |
| `hiddenNamespaces` | `[]` | Masque des namespaces entiers, par exemple `["create"]`. |
| `hiddenAdvancements` | `[]` | Masque des progrès précis, par exemple `["minecraft:story/mine_stone"]`. |
| `whitelist` | `[]` | Ids ou namespaces toujours affichés, quels que soient les filtres ci-dessus. |

`advancements.enabled = false` masque tous les messages de progrès, y compris ceux de la liste blanche.

### Variables des messages

| Section | Variable | Valeur |
|---|---|---|
| `join`, `firstJoin`, `leave` | `{player}` | Nom d'utilisateur |
| | `{display_name}` | Nom affiché, avec l'éventuel surnom ou préfixe d'équipe |
| | `{online}`, `{max}` | Joueurs en ligne, nombre maximum de joueurs |
| `advancements` | `{player}` | Nom d'utilisateur |
| | `{advancement}` | Titre traduit, avec le texte au survol de vanilla |
| | `{advancement_id}` | Id, par exemple `minecraft:story/mine_stone` |
| | `{type}` | `task`, `goal` ou `challenge` |
| `death` | `{player}` | Nom d'utilisateur |
| | `{death_message}` | Message de mort vanilla, traduit |
| | `{dimension}` | Id, par exemple `minecraft:the_nether` |
| | `{x}`, `{y}`, `{z}` | Position de la mort |

Une variable qui n'existe pas pour la section s'affiche telle quelle.

### Codes couleur

Utilise les codes standard de Minecraft avec `&` :

- **Couleurs :** `&0` à `&9`, `&a` à `&f`
- **Formats :** `&k` illisible, `&l` gras, `&m` barré, `&n` souligné, `&o` italique
- **Réinitialisation :** `&r`

Une couleur annule les formats placés avant elle, écris donc la couleur en premier : `&e&l`, pas `&l&e`.

Une couleur placée avant `{advancement}` remplace sa couleur vanilla. Mets `&r` devant pour garder le vert vanilla, ou le violet des défis.

### Alias

La section `[aliases]` remplace les valeurs de `{dimension}`, `{type}` et `{advancement_id}`, et seulement celles-là.

- Le préfixe `minecraft:` est facultatif. Les ids des autres mods doivent être écrits en entier.
- Les clés contenant `:` ou `/` ont besoin de guillemets.
- Les codes couleur ne fonctionnent pas dans un alias. Mets la couleur dans le message, avant la variable.

### Exemple

```toml
logToConsole = true

[join]
	enabled = true
	mode = "CUSTOM"
	customMessage = "&a+ &f{display_name} &7({online}/{max})"

[firstJoin]
	enabled = true
	customMessage = "&6Bienvenue à {display_name}, qui se connecte pour la première fois !"

[leave]
	enabled = false

[advancements]
	enabled = true
	mode = "VANILLA"

	[advancements.filters]
		hideTasks = true
		whitelist = ["minecraft:story/mine_diamond"]

[death]
	enabled = true
	mode = "CUSTOM"
	customMessage = "&c{death_message} &7dans {dimension} ({x}, {y}, {z})"

[aliases]
	overworld = "l'Overworld"
	the_nether = "le Nether"
	the_end = "l'End"
	"mymod:my_dimension" = "ma dimension"
```

### Contribuer

Les signalements de bugs et les idées sont les bienvenus dans les [issues](https://github.com/SacBruine237275/messages-control/issues). Avant d'ouvrir une pull request, lis [CONTRIBUTING.md](CONTRIBUTING.md).

### Sécurité

Pour signaler une vulnérabilité, consulte [SECURITY.md](SECURITY.md). Merci de ne pas ouvrir d'issue publique.

### Licence

Ce mod est distribué sous [licence MIT](LICENSE).

Ce projet n'est pas affilié à Mojang, Microsoft ou NeoForged.
