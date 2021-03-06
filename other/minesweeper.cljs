;; This code is from http://sneakycode.net/minesweeper
;; Minesweeper implemented using clojurescript and P5 js

(ns mine.core)

(defn position [col-count i]
  "position takes the column count and the index and returns the column and row of the cell"
  (let [col (mod i col-count)
        row (/ (- i col) col-count)]
    [col row]))

(defn index
  "index takes the col count, row and column and returns the index of the cell"
  [col-count row col]
  (+ col (* row col-count)))

(defn neighbors
  "neighbors returns the neighboring cell index around a cell"
  [row-count col-count cell]
  (let [{:keys [i row col]} cell
        row-                (dec row)
        row+                (inc row)
        col-                (dec col)
        col+                (inc col)
        neighbors           [[col- row-] [col row-] [col+ row-]
                             [col- row]             [col+ row]
                             [col- row+] [col row+] [col+ row+]]]
    (->> neighbors
         (filter (fn [[col row]]
                   (and (>= row 0) (>= col 0)
                        (< row row-count) (< col col-count))))
         (map (fn [[col row]]
                (index col-count row col))))))

(defn cell
  "cell creates a hash-map of cell data"
  [col-count i]
  (let [[col row] (position col-count i)]
    {:i        i :row row :col col
     :type     :empty
     :flagged? false
     :opened?  false}))

(defn init-cells
  "init-cells creates all the cells in a map for the gameboard with an identity"
  [row-count col-count]
  (let [size (* row-count col-count)]
    (->> (range size)
         (map #(cell col-count %))
         (map (juxt :i identity))
         (into {}))))

(defn place-bombs
  "places bombs randomly thoughout the cells-map"
  [bomb-count cells-map]
  (loop [bomb-count  bomb-count
         empty-cells (set (keys cells-map))
         result      cells-map]
    (if (<= bomb-count 0)
      result
      (let [random-index (rand-int (count empty-cells))
            bomb-index   (nth (vec empty-cells) random-index)]
        (recur
         (dec bomb-count)
         (disj empty-cells bomb-index)
         (assoc-in result [bomb-index :type] :bomb))))))

(defn set-neighbors
  "set-neighbors fills out the neighboring cell indexs and bome neighbors"
  [row-count col-count cells-map]
  (->> cells-map
       (map (fn [[i cell]]
              (let [neighbors     (neighbors row-count col-count cell)
                    bombs-touching (->> neighbors
                                        (filter #(= :bomb (get-in cells-map [% :type])))
                                        count)]
                [i
                 (assoc cell
                        :neighbors neighbors
                        :bombs-touching bombs-touching
                        :type (cond
                                (= :bomb (:type cell)) :bomb
                                (> bombs-touching 0)   :bomb-adjacent
                                :else                  (:type cell)))])))
       (into {})))

(defn board
  "Creates a game board map with cells, bombs and neighbors filled out"
  [row-count col-count bomb-count]
  (->> (init-cells row-count col-count)
       (place-bombs bomb-count)
       (set-neighbors row-count col-count)))

(defn adjacent-open-cells
  [board i]
  (loop [cells-to-check #{i}
         cells-checked  #{}
         cells-to-open  []]
    (if (empty? cells-to-check)
      cells-to-open
      (let [i                                     (first cells-to-check)
            {:keys [neighbors opened?] :as cell} (get board i)
            t                                     (:type cell)
            should-open?                          (and (not= :bomb t) (not opened?))]
        (if-not should-open?
          (recur (disj cells-to-check i)
                 (conj cells-checked i)
                 cells-to-open)
          (let [cells-not-to-check (into cells-checked cells-to-check)
                neighbors-to-add  (when (= :empty t)
                                    (filter #(not (contains? cells-not-to-check %))  neighbors))]
            (recur (into (disj cells-to-check i) neighbors-to-add)
                   (conj cells-checked i)
                   (conj cells-to-open i))))))))

(defn toggle-cell-flag [board i]
  (update-in board [i :flagged?] not))

(defn detonate [board {:keys [i] :as cell}]
  (->> (assoc-in board [i :killer?] true)
       (map (fn [[i cell]]
              [i (assoc cell :opened? true)]))
       (into {})))

(defn open-adjacent-cell [board i]
  (assoc-in board [i :opened?] true))

(defn open-empty-cell [board i]
  (reduce
   (fn [b i] (open-adjacent-cell b i))
   board
   (adjacent-open-cells board i)))

(defn not-opened-non-bomb-cell? [cell]
  (and (false? (:opened? cell)) (not= :bomb (:type cell))))

(defn win? [board]
  (->> board
       vals
       (filter not-opened-non-bomb-cell?)
       empty?))

(defn open-cell [{:keys [board] :as state} i]
  (let [cell   (get board i)
        bomb?  (= :bomb (:type cell))
        empty? (= :empty (:type cell))
        next-board
        (cond
          bomb?  (detonate board cell)
          empty? (open-empty-cell board i)
          :else  (open-adjacent-cell board i))
        win?   (win? next-board)]
    (merge state
           {:board next-board
            :dead? bomb?
            :win?  win?})))

(defn new-game [rows cols bombs]
  {:board (board rows cols bombs)
   :win?  false
   :dead? false
   :rows  rows
   :cols  cols
   :bombs bombs
   :size  35})

;;;; GAME

(defonce *state (atom nil))

(defn reset-game []
  (let [{:keys [rows cols bombs]} @*state]
    (reset! *state (new-game rows cols bombs))))

(defn mouse-clicked []
  (let [{:keys [rows cols size]} @*state
        mx                       js/mouseX
        my                       js/mouseY
        in-bounds?               (and (pos? mx) (pos? my) (< mx (* size cols)) (< my (* size rows)))
        col                      (js/Math.floor (/ mx size))
        row                      (js/Math.floor (/ my size))
        i                        (index cols row col)
        ctrl-pressed?            (= 16 (when (js/keyIsDown 16) js/keyCode))
        dead?                    (:dead? @*state)
        win?                     (:win? @*state)
        f                        (cond
                                   (or dead? win?)  #(reset-game)
                                   (not in-bounds?) identity
                                   ctrl-pressed?    #(update % :board toggle-cell-flag i)
                                   :else            #(open-cell % i))]
    (swap! *state f)))

(defn ^:export easy-game []
  (reset! *state (new-game 10 10 10)))

(defn ^:export medium-game []
  (reset! *state (new-game 16 16 40)))

(defn ^:export hard-game []
  (reset! *state (new-game 16 30 99)))

(defn ^:export phone-game []
  (reset! *state (new-game 8 8 8)))

;;;; DRAW
(defonce *canvas (atom nil))

(defn setup []
  (easy-game)
  (let [{:keys [rows cols size]} @*state
        canvas (js/createCanvas (+ 1 (* cols size)) (+ 1 (* rows size)))]
    (.parent canvas "game")
    (js/rectMode "CENTER")
    (js/noStroke)
    (reset! *canvas canvas)
    (add-watch *state "redraw" #(js/redraw))))

(defn draw-initial-cell []
  (if (:win? @*state)
    (js/fill 0 255 0)
    (js/fill 240))
  (let [{:keys [size]} @*state]
    (js/rect 0 0 size size)))

(defn draw-flagged-cell []
  (draw-initial-cell)
  (js/fill 255 150 0)
  (js/noStroke)
  (let [{:keys [size]} @*state]
    (js/ellipse (/ size 2) (/ size 2) (/ size 3) (/ size 3))))

(defn draw-empty-cell []
  (js/fill 150)
  (let [{:keys [size]} @*state]
    (js/rect 0 0 size size)))

(defmulti draw-cell :type)

(defmethod draw-cell :empty [_]
  (draw-empty-cell))

(defmethod draw-cell :bomb [_]
  (draw-empty-cell)
  (js/fill 255 0 0)
  (js/noStroke)
  (let [{:keys [size]} @*state]
    (js/ellipse (/ size 2) (/ size 2) (/ size 2) (/ size 2))))

(defmethod draw-cell :bomb-adjacent [cell]
  (draw-empty-cell)
  (js/noStroke)
  (js/fill (* 50 (:bombs-touching cell)) 0 0)
  (js/textSize 15)
  (let [{:keys [size]} @*state]
    (js/text (str (:bombs-touching cell)) (/ size 2.2) (/ size 1.5))))

(defn draw []
  (let [{:keys [size board cols rows]} @*state]
    (.resize @*canvas (inc (* cols size)) (inc (* rows size)))
    (doseq [[i {:keys [row col flagged? opened?] :as cell}] board]
      (js/push)
      (js/stroke 180)
      (js/translate (* col size) (* row size))
      (cond
        opened?  (draw-cell cell)
        flagged? (draw-flagged-cell)
        :else    (draw-initial-cell))
      (js/pop)))
  (js/noLoop))

;;;; INIT

(doto js/window
  (aset "setup" setup)
  (aset "draw" draw)
  (aset "touchStarted" mouse-clicked))