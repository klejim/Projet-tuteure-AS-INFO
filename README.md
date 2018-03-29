# Générer la javadoc

Ouvrez un terminal depuis la racine du projet et entrez les commandes suivantes:
```
cd src  
javadoc -d ../docs/html project -subpackages project.network:project.view -private -overview project/doc-files/overview.html  
cd ../  
```

Vous pouvez lire la documentation hors-ligne en ouvrant docs/index.html.