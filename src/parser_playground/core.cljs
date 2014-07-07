(ns parser-playground.core
  (:require [parser-playground.parser :as parser]
            [om.core :as om :include-macros true]
            [om.dom :include-macros true]
            [om-tools.core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [weasel.repl :as ws-repl]))

(enable-console-print!)

(ws-repl/connect "ws://localhost:9001")


(def test-grammar {:expr '[:num [* "+" :num]]
                   :num  '[[+ [| \0 \1 \2 \3 \4 \5 \6 \7 \8 \9]]
                           [* (| \space \tab)]]})

(defn parse-num [input]
  (let [digits [\0 \1 \2 \3 \4 \5 \6 \7 \8 \9]
        spaces [\space \tab]]
    (if (some #(= (first input) %) digits)
      (let [captured-digits (loop [input input captured-digits []]
                              (if (some #(= (first input) %) digits)
                                (recur (.substring input 1) (conj captured-digits (first input)))
                                captured-digits))
            input (.substring input (count captured-digits))
            input-after-trimming (loop [input input]
                                   (if (some #(= (first input) %) spaces)
                                     (recur (.substring input 1))
                                     input))]
        [[:num captured-digits] input-after-trimming])
      [:fail input])))

(defn parse-expr [input]
  (let [[num input] (parse-num input)]
    [num input]))

(def p (parser/parser test-grammar :expr))

(def app-state (atom {:grammar ""
                      :input ""
                      :output ""}))

(defcomponent app [{:keys [grammar input output] :as state} owner]
  (display-name [_] "App")

  (render [_]
    (dom/div
      (dom/h1 "Parser Playground")
      (dom/form
        (dom/div
          (dom/label "Grammar"
                     (dom/br nil)
                     (dom/textarea {:value grammar
                                    :style {:width "300px"
                                            :height "200px"}
                                    :onChange #(om/update! state [:grammar] (.. % -target -value))})))
        (dom/div
          (dom/label "Input"
                     (dom/br nil)
                     (dom/textarea {:value input
                                    :style {:width "300px"
                                            :height "200px"}
                                    :onChange #(om/update! state [:input] (.. % -target -value))})))
        (dom/p output)))))


(om/root
  app
  app-state
  {:target (. js/document (getElementById "app"))})

