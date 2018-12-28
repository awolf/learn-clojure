

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

;; Metadata

(defn ^{:tag String} shout [^{:tag String} s] (clojure.string/upper-case s))

(defn ^String shout-loud [^String s] (clojure.string/upper-case s))

;; Calling Java

(new java.util.Random)

(java.util.Random.)

(def rnd (new java.util.Random))

(. rnd nextInt)

(. rnd nextInt 10)

(def p (java.awt.Point. 10 20))
(. p x)

(. System lineSeparator)

(. Math PI)

;; a more concise syntax

(.nextInt rnd 10)

(.x p)

(System/lineSeparator)

(javadoc java.net.URL)

;; Comments

;; this is a comment

(comment
  (defn ignore-me []
  ;; not done yet
))

(defn triple [number]
  #_(println "debug triple" number)
  (* 3 number))

(triple 11)

;; Flow Control

(defn is-small? [number]
  (if (< number 100) "yes"))

(defn is-small2? [number]
  (if (< number 100) "yes" "no"))

(defn is-small3? [number]
  (if (< number 100)
    "yes"
    (do
      (println "Saw a big number " number)
      "no")))

;; Recure with Loop/Recur

(loop [result [] x 5]
  (if (zero? x)
    result
    (recur (conj result x) (dec x))))

(defn countdown [result x]
  (if (zero? x)
    result
    (recur (conj result x) (dec x))))

;; Where's my for loop?

(defn indexed [coll] (map-indexed vector coll))

(defn index-filter [pred coll]
  (when pred
    (for [[idx elt] (indexed coll) :when (pred elt)] idx)))

(index-filter #{\a \b} "abcdbbb")

(defn index-of-any [pred coll]
  (first (index-filter pred coll)))

(index-of-any #{\a \b} "zabcdbbb")