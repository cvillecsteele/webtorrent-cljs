(ns webtorrent-cljs.buffer
  (:refer-clojure :exclude [remove get])
  (:require-macros
   [cljs.core.async.macros :refer [go]])
  (:require
   [buffer]
   [clojure.walk :as walk]
   [cljs.core.async :as async]
   [webtorrent-cljs.util :refer [with-callback]]))

(enable-console-print!)

(defn from-string
  ""
  [str]
  (js/buffer.Buffer. str))
