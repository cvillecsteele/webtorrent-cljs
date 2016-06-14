(ns webtorrent-cljs.util
  (:require-macros
   [cljs.core.async.macros :refer [go]])
  (:require
   [clojure.walk :as walk]
   [cljs.core.async :as async]
   [webtorrent]
   [buffer]))

(enable-console-print!)

(defn- make-handler [ch]
  (fn [& data]
    (go
      (if data (async/>! ch (map (comp js->clj walk/keywordize-keys) data)))
      (async/close! ch))))

(defn with-callback [f]
  (let [ch (async/chan)]
    (f (make-handler ch))
    ch))

