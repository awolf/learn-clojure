
(def failed-protagonist-names
  ["Larry Potter" "Doreen the Explorer" "The Incredible Bulk"])

failed-protagonist-names

(defn error-message
  [severity]
  (str "OH GOD! IT'S A DISASTER! WE'RE "
       (if (= severity :mild)
         "MILDLY INCONVENIENCED!"
         "DOOOOOOOMED!")))

(error-message :mild)

(def name "Chewbacca")
(str "\"Uggllglglglglglglglll\" - " name)

{:first-name "Charlie"
 :last-name "McFishwich"}

(def myhashmap {:name {:first "John" :middle "Jacob" :last "Jingleheimerschmidt"}})

(get myhashmap :name)
(get myhashmap :name)
(get-in myhashmap [:name :middle])
(myhashmap :name)
(:name1 myhashmap "NoName")

