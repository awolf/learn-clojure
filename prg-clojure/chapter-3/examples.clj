


(first `(1 2 3))

(rest `(1 2 3))

(cons 0 `(1 2 3))

(first [1 2 3])

(rest [1 2 3])

(cons 0 [1 2 3])

(seq? (rest [1 2 3]))

(first {:fname "Adam" :lname "Wolf"})

(rest {:fname "Adam" :lname "Wolf"})

(cons [:mname "Jay"] {:fname "Adam" :lname "Wolf"})

(first #{:the :quick :brown :fox})

(rest #{:the :quick :brown :fox})

(cons :jumped #{:the :quick :brown :fox})

(sorted-set :the :quick :brown :fox)

(sorted-map :c 3 :b 2 :a 1)

(conj `(1 2 3) :a)

(into `(1 2 3) `(:a :b :c))

(conj [1 2 3] :a)

(into [1 2 3] [:a :b :c])

(list? (rest [1 2 3]))

(seq? (rest [1 2 3]))

;; Using the Sequence Library

;; Creating Sequences

(range 10)

(range 10 20)

(range 1 25 2)

(range 0 -1 -0.25)

(range 1/2 4 1)

(repeat 5 1)

(repeat 10 "x")

(take 10 (iterate inc 1))

(def whole-numbers (iterate inc 1))

(take 20 whole-numbers)

(take 10 (cycle (range 3)))

(interleave whole-numbers ["A" "B" "C" "D" "E"])

(interpose "," ["apples" "bannanas" "grapes"])

(apply str (interpose "," ["apples" "bannanas" "grapes"]))

(require '[clojure.string :refer [join]])
(join \, ["apples" "bannanas" "grapes"])

(set [1 2 3])

(hash-set 1 2 3)

(vec (range 3))

;; Filtering Sequences

(take 10 (filter even? whole-numbers))

(take 10 (filter odd? whole-numbers))

(def vowel? #{\a \e \i \o \u})
(def constant? (complement vowel?))

(vowel? \a)

(take-while constant? "the-quick-brown-fox")

(drop-while constant? "the-quick-brown-fox")

(split-at 5 (range 10))

(split-with #(<= % 10) (range 0 20 2))

;; Sequence Predicates

(every? odd? [1 3 5])

(every? odd? [1 3 5 8])

(some even? [1 2 3])

(some even? [1 3 5])

(some identity [nil false 1 nil 2])

(some #{3} (range 20))

(not-every? even? whole-numbers)

(not-any? even? whole-numbers)

;; Transforming Sequences

(map #(format "<p>%s</p>" %) ["the" "quick" "brown" "fox"])

(map #(format "<%s>%s</%s>" %1 %2 %1) ["h1" "h2" "h3" "h1"] ["the" "quick" "brown" "fox"])

(reduce + (range 1 11))

(reduce * (range 1 11))

(sort [42 1 7 11])

(sort-by #(.toString %) [42 2 7 11])

(sort > [42 1 7 11])

(sort-by :grade > [{:grade 83} {:grade 90} {:grade 77}])

(for [word ["the" "quick" "brown" "fox"]] (format "<p>%s</p>" word))

(take 10 (for [n whole-numbers :when (even? n)] n))

(for [n whole-numbers :while (even? n)] n)

(for [file "ABCDEFGH" rank (range 1 9)] (format "%c%d" file rank))

(take 10 (for [file "ABCDEFGH" rank (range 1 9)] (format "%c%d" file rank)))

;; Lazy and Infinite Sequences

