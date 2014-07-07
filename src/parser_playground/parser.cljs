(ns parser-playground.parser)

(defrecord Parser [grammar expression pos]
  IFn
  (invoke [_ input]
    (.log js/console input)))

(defn parser [grammar start-expression]
  (->Parser grammar start-expression 0))
