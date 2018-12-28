

; Vector
[1 2 3]

; List
(quote (1 2 3))

'(1 2 3)

; Set
#{1 2 3 5}

; Maps

{"Lisp" "McCarthy" "Clojure" "Hickey"}
{:Lisp "McCarthy" :Clojure "Hickey"}

(defrecord Book [title author])
(->Book "History of the world" "Mel Brooks")

; Functions

(defn greeting
  "Returns a greeting of the form 'Hello, username.'"
  [username]
  (str "Hello, " username))

(defn greeting
  "Returns a greeting of the for 'Hello, username.'
    default username is 'world'."
  ([] (greeting "world"))
  ([username] (str "Hello, " username)))

(defn date [person-1 person-2 & chaperones]
  (println person-1 "and" person-2
           "went out with" (count chaperones) "chaperones."))

; Anonymous Functions

(defn indexable-word? [word]
  (> (count word) 2))

(require '[clojure.string :as str])
(filter indexable-word? (str/split "A fine day it is" #"\W+"))

; with anonymous functions
(filter (fn [w] (> (count w) 2)) (str/split "A fine day it is" #"\W+"))
(filter #(> (count %) 2) (str/split "A fine day it is" #"\W+"))

(defn indexable-words [text]
  (let [indexable-word? (fn [w] (> (count w) 2))]
    (filter indexable-word? (str/split text #"\W+"))))

(indexable-words "a fine day it is, Mate!")

(defn make-greeter [greeting-prefix]
  (fn [username] (str greeting-prefix ", " username)))

; please excuse me from bro-ing out
(def brooo (make-greeter "Brooooo"))
(brooo "adam")

((make-greeter "Howdy") "partner")

;; Vars, Bindings and Namespaces
(def foo 10)
(var foo)
#'foo

(defn square-corners [bottom left size]
  (let [top (+ bottom size)
        right (+ left size)]
    [[bottom left] [top left] [top right] [bottom right]]))

; Destructuring

(defn greet-author-1 [author]
  (println "Hello, " (:first-name author)))
(greet-author-1 {:last-name "Vinge" :first-name "Vernor"})

(defn greet-author-2 [{fname :first-name}]
  (println "Hello, " fname))
(greet-author-2 {:last-name "Vinge" :first-name "Vernor"})

(let [[x y] [1 2 3]]
  [x y])

(let [[_ _ z] [1 2 3]]
  z)

(let [[x y :as coords] [1 2 3 4 5 6]]
  (str "x: " x ",y: " y ",total dimensions " (count coords)))

(require '[clojure.string :as str])
(defn ellipsize [words]
  (let [[w1 w2 w3] (str/split words #"\s+")]
    (str/join " " [w1 w2 w3 "..."])))

(ellipsize "The quick brown fox jumped over the lazy dog.")

; Namespaces

(resolve 'ellipsize)

(in-ns 'myapp)
(resolve 'ellipsize)
(def wolf 100)
(resolve 'adam)