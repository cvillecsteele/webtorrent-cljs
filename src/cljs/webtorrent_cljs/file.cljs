(ns webtorrent-cljs.file
  (:require-macros
   [cljs.core.async.macros :refer [go]])
  (:require
   [clojure.walk :as walk]
   [cljs.core.async :as async]
   [webtorrent-cljs.util :refer [with-callback]]))

(enable-console-print!)

(defn info
  "See https://webtorrent.io/docs.  Get file info."
  [t]
  {:name (aget t "name")
   :path (aget t "path")
   :length (aget t "length")})

;; file.select()
(defn select
  "See https://webtorrent.io/docs.  Select a file"
  [f]
  (.select f))

;; file.deselect()
(defn deselect
  "See https://webtorrent.io/docs.  De-select a file"
  [f]
  (.deselect f))

;; file.createReadStream([opts])
(defn create-read-stream
  "See https://webtorrent.io/docs."
  [f opts]
  (.createReadStream f (clj->js opts)))

;; file.getBuffer(function callback (err, buffer) {})
(defn get-buffer
  "See https://webtorrent.io/docs.
  Returns a channel, written to once the file is ready"
  [f]
  (with-callback
    #(.getBuffer f %)))

;; file.appendTo(rootElem, [function callback (err, elem) {}])
(defn append-to
  "See https://webtorrent.io/docs.
  Returns a channel written to once the file is visible to the user."
  [f root]
  (with-callback
    #(.appendTo f root %)))

;; file.renderTo(elem, [function callback (err, elem) {}])
(defn render-to
  "See https://webtorrent.io/docs.
  Like file.appendTo but renders directly into given element (or CSS selector)."
  [f elem]
  (with-callback
    #(.renderTo f elem %)))

;; file.getBlobURL(function callback (err, url) {})
(defn get-blob-url
  "See https://webtorrent.io/docs.
  Get a url which can be used in the browser to refer to the file."
  [f elem]
  (with-callback
    #(.getBlobURL f %)))

