
; bad idea

(defn stack-consuming-fibo [n]
  (cond
    (= n 0) 0
    (= n 1) 1
    :else (+ (stack-consuming-fibo (- n 1))
             (stack-consuming-fibo (- n 2)))))

(stack-consuming-fibo 9)
 ;; stackoverflow (stack-consuming-fibo 1000000)

(defn tail-fibo [n]
  (letfn [(fib
            [current next n]
            (if (zero? n)
              current
              (fib next (+ current next) (dec n))))]
    (fib 0N 1N n)))

(tail-fibo 9)
;; stackoverflow (tail-fibo 10000009)

(defn recur-fibo [n]
  (letfn [(fib
            [current next n]
            (if (zero? n)
              current
              (recur next (+ current next) (dec n))))]
    (fib 0N 1N n)))

(recur-fibo 9)
(recur-fibo 1000000)

;; Lazy Sequences

(defn lazy-seq-fibo
  ([]
   (concat [0 1] (lazy-seq-fibo 0N 1N)))
  ([a b]
   (let [n (+ a b)]
     (lazy-seq
      (cons n (lazy-seq-fibo b n))))))

(take 10 (lazy-seq-fibo))
(rem (nth (lazy-seq-fibo) 1000000) 1000)

(take 5 (iterate (fn [[a b]] [b (+ a b)]) [0 1]))

(defn fibo []
  (map first (iterate (fn [[a b]] [b (+ a b)]) [0N 1N])))

;; Coming to Realization

(def lots-o-fibs (take 1000000000 (fibo)))
(nth lots-o-fibs 100)

;; Losing your head

;; Lazier than lazy

(defn count-heads-pairs [coll]
  (loop [cnt 0 coll coll]
    (if (empty? coll)
      cnt
      (recur (if (= :h (first coll) (second coll))
               (inc cnt)
               cnt)
             (rest coll)))))

(count-heads-pairs [:h :h :h :t :h])
(count-heads-pairs [:h :t :h :t :h])
(count-heads-pairs [:h :h :h :h :h])

;; Transforming the input sequence
(declare my-odd? my-even?)

(defn my-odd? [n]
  (if (= n 0)
    false
    (my-even? (dec n))))

(defn my-even? [n]
  (if (= n 0)
    true
    (my-odd? (dec n))))

(map my-even? (range 10))
(map my-odd? (range 10))

;; Egar Transformations

(defn square [x] (* x x))

(defn sum-squares-seq [n]
  (vec (map square (range n))))

(defn sum-squares 
    [n]
    (into [] (map square)(range n)))


