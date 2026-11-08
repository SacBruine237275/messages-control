# Contributing / Contribuer

- [English](#english)
  - [Branches](#branches)
  - [Making a change](#making-a-change)
  - [Fixing another Minecraft version](#fixing-another-minecraft-version)
  - [Releases](#releases)
  - [Supporting a new Minecraft version](#supporting-a-new-minecraft-version)
- [Français](#français)
  - [Les branches](#les-branches)
  - [Faire une modification](#faire-une-modification)
  - [Corriger une autre version de Minecraft](#corriger-une-autre-version-de-minecraft)
  - [Les releases](#les-releases)
  - [Prendre en charge une nouvelle version de Minecraft](#prendre-en-charge-une-nouvelle-version-de-minecraft)

---

## English

### Branches

| Branch | Purpose | Merges into |
|---|---|---|
| `main` | Latest stable release, for the newest supported Minecraft version | Nothing |
| `dev` | Next release of `main`. Finished work waits here. | `main` |
| `mc/<version>` | Maintained release for an older Minecraft version, for example `mc/1.21.1` | Nothing |
| `feat/...`, `fix/...`, `refactor/...`, `docs/...` | A single change | `dev`, or `mc/<version>` for a version-specific fix |

Don't push directly to `main`, `dev` or `mc/*`: open a pull request. Delete short-lived branches once they are merged.

### Making a change

You need JDK 21.

```sh
git switch dev
git pull
git switch -c feat/my-change
./gradlew build
```

Open a pull request to `dev` and describe what you tested in game. The build must pass.

### Fixing another Minecraft version

A change that also applies to an older Minecraft version is copied after it is merged in `dev`:

```sh
git switch mc/1.21.1
git pull
git switch -c fix/my-fix-mc1.21.1
git cherry-pick <commit>
```

Open a pull request to `mc/1.21.1`. A bug that only exists on this version starts from the same branch, without the cherry-pick.

### Releases

Every push to `main` or `mc/*` builds the mod and publishes a GitHub release, unless its tag already exists. The tag comes from `gradle.properties`:

```text
v<mod_version>-mc<minecraft_version>    for example v1.0.0-mc1.21.11
```

1. Raise `mod_version` in the pull request. If the tag already exists, nothing is published and the existing release doesn't change.
2. For `main`, open a pull request from `dev` and merge it with a merge commit, not a squash.
3. Test the jar, then publish it on Modrinth by hand.

The same `mod_version` on two branches means the same features.

### Supporting a new Minecraft version

1. Create the branch for the current version from `main`, for example `mc/1.21.11`, so that it can still receive fixes.
2. On a branch from `dev`, update `minecraft_version`, `minecraft_version_range`, `neo_version` and the NeoForge range in `neoforge.mods.toml`.
3. Test in game, then release through `main`.

---

## Français

### Les branches

| Branche | Rôle | Merge vers |
|---|---|---|
| `main` | Dernière version stable, pour la version de Minecraft la plus récente prise en charge | Aucune |
| `dev` | Prochaine version de `main`. Le travail terminé attend ici. | `main` |
| `mc/<version>` | Version maintenue pour une ancienne version de Minecraft, par exemple `mc/1.21.1` | Aucune |
| `feat/...`, `fix/...`, `refactor/...`, `docs/...` | Une seule modification | `dev`, ou `mc/<version>` pour un correctif propre à cette version |

Ne pousse jamais directement sur `main`, `dev` ou `mc/*` : ouvre une pull request. Supprime les branches temporaires une fois mergées.

### Faire une modification

Il te faut le JDK 21.

```sh
git switch dev
git pull
git switch -c feat/ma-modification
./gradlew build
```

Ouvre une pull request vers `dev` et indique ce que tu as testé en jeu. Le build doit passer.

### Corriger une autre version de Minecraft

Une modification qui concerne aussi une ancienne version de Minecraft est copiée une fois mergée dans `dev` :

```sh
git switch mc/1.21.1
git pull
git switch -c fix/mon-correctif-mc1.21.1
git cherry-pick <commit>
```

Ouvre une pull request vers `mc/1.21.1`. Un bug propre à cette version part de la même branche, sans le cherry-pick.

### Les releases

Chaque push sur `main` ou `mc/*` compile le mod et publie une release GitHub, sauf si son tag existe déjà. Le tag vient de `gradle.properties` :

```text
v<mod_version>-mc<minecraft_version>    par exemple v1.0.0-mc1.21.11
```

1. Augmente `mod_version` dans la pull request. Si le tag existe déjà, rien n'est publié et la release existante ne change pas.
2. Pour `main`, ouvre une pull request depuis `dev` et merge-la avec un merge commit, pas un squash.
3. Teste le jar, puis publie-le à la main sur Modrinth.

Un même `mod_version` sur deux branches signifie les mêmes fonctionnalités.

### Prendre en charge une nouvelle version de Minecraft

1. Crée la branche de la version actuelle à partir de `main`, par exemple `mc/1.21.11`, pour qu'elle puisse encore recevoir des correctifs.
2. Sur une branche issue de `dev`, change `minecraft_version`, `minecraft_version_range`, `neo_version` et la plage NeoForge dans `neoforge.mods.toml`.
3. Teste en jeu, puis publie en passant par `main`.
