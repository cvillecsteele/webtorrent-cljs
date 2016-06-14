(ns webtorrent-cljs.torrent
  (:require-macros
   [cljs.core.async.macros :refer [go]])
  (:require
   [clojure.walk :as walk]
   [cljs.core.async :as async]
   [webtorrent-cljs.util :refer [with-callback]]))

(enable-console-print!)

(defn info
  "See https://webtorrent.io/docs.  Get torrent info."
  [t]
  {:info-hash (aget t "infoHash")
   :magnet-uri (aget t "magnetURI")
   :torrent-file (aget t "torrentFile")
   :time-remaining (aget t "timeRemaining")
   :received (aget t "received")
   :downloaded (aget t "downloaded")
   :uploaded (aget t "uploaded")
   :download-speed (aget t "downloadSpeed")
   :upload-speed (aget t "uploadSpeed")
   :progress (aget t "progress")
   :path (aget t "path")
   :ratio (aget t "ratio")
   :num-peers (aget t "numPeers")
   :torrent-file-blob-url (aget t "torrentFileBlobURL")})

;; torrent.destroy([function callback (err) {}])
(defn destroy
  "See https://webtorrent.io/docs.  Destroy the client.
   Returns a channel written to when the torrent is fully destroyed,
  i.e. all open sockets are closed, and the storage is closed."
  [t]
  (with-callback
    #(.destroy t %)))

;; torrent.addPeer(peer)
(defn add-peer
  "See https://webtorrent.io/docs.  Add a peer."
  [t peer]
  (.addPeer t peer))

;; torrent.removePeer(peer)
(defn remove-peer
  "See https://webtorrent.io/docs.  Remove a peer."
  [t peer]
  (.removePeer t peer))

;; torrent.addWebSeed(url)
(defn add-web-seed
  "See https://webtorrent.io/docs.  Add a seed."
  [t url]
  (.addWebSeed t url))

;; torrent.select(start, end, [priority], [notify])
(defn select
  "See https://webtorrent.io/docs.
  Selects a range of pieces to prioritize starting with start and
  ending with end (both inclusive) at the given priority.  Returns a channel,
  written to when the selection is updated with new data."
  [t start end priority]
  (with-callback
    #(.select t start end priority %)))

;; torrent.deselect(start, end, priority)
(defn deselect
  "See https://webtorrent.io/docs.
  Deprioritizes a range of previously selected pieces."
  [t start end priority]
  (.deselect t start end priority))

;; torrent.critical(start, end)
(defn critical
  "See https://webtorrent.io/docs.
  Marks a range of pieces as critical priority to be downloaded
  ASAP. From start to end (both inclusive)."
  [t start end]
  (.critical t start end))

;; torrent.createServer([opts])
(defn create-server
  "See https://webtorrent.io/docs."
  [t opts]
  (.createServer t (clj->js opts)))

(defn pause
  "See https://webtorrent.io/docs."
  [t]
  (.pause t))

(defn resume
  "See https://webtorrent.io/docs."
  [t]
  (.resume t))

;; torrent.on('infoHash', function () {})
;; torrent.on('metadata', function () {})
;; torrent.on('ready', function () {})
;; torrent.on('warning', function (err) {})
;; torrent.on('error', function (err) {})
;; torrent.on('done', function () {})
;; torrent.on('download', function (bytes) {})
;; torrent.on('upload', function (bytes) {})
;; torrent.on('wire', function (wire) {})
;; torrent.on('noPeers', function (announceType) {})
(defn on
  "See https://webtorrent.io/docs.  Set a callback.
   Returns a channel written to when the event triggers."
  [t msg]
  (with-callback
    #(.on t msg %)))

