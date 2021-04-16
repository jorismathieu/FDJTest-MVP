# FDJTest-MVP

Architecture : MVP en respectant les principes de clean archi

4 modules : app / business (kotlin) / data (kotlin) / presentation

Le module business ne dépend de rien

Le module data ne dépend que de business

Le module presentation ne dépend que de business

Le module app fait juste le lien entre le tout pour l'injection des dépendances

![Arch diagram](arch_diagram.png)

Technos utilisées :
- Koin : pour l'injection de dépendance, le code s'adaptera facilement à un autre SDK (Dagger, Hilt, Service locator "maison" etc...)
- Retrofit/Okhttp/Gson : pour la partie data
- Glide
- Coroutines
- Les libs classiques pour la vue


Points d'améliorations possibles :
- Faire le bonus de l'auto completion ;)
- Ajouter du cache objet via une local data source
- On a déjà toutes les données nécessaire sur une équipe lors du premier appel, on pourrait donc se passer de faire le deuxième appel pour afficher le détail
- Mettre en place un refresh en cas d'erreur
- Tests :
  * J'ai testé unitairement uniquement la partie business, mais tout le reste est testable également sur le même principe
  * Ajouter des tests d'intégration entre : fragment/presenter, presenter/use case, repository/data source
  * On peut évidemment aussi ajouter des tests fonctionnels
- Passer sur du MVVM, sur Android ça permet de ne pas trop de soucier des problèmes de cycle de vie
- Avec quelques tweaks, il est possible rendre les presenters totalement indépendants de la plateforme

