
## Cheat Sheet

To connect to nrepl inside vscode 
```
lein repl :connect 51381
```

Send Clojure expressions to repl from VS Code
```
shift + command + e
```

Save some work in temp.clj, and then ... 
```
user=> (load-file "temp.clj")
```

This require has a path in the namespace name and a file name.
\code\src\examples\introduction.clj
```
user=> (require 'code.src.examples.introduction) 
```