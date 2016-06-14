(ns webtorrent-cljs.client
  (:refer-clojure :exclude [remove get])
  (:require-macros
   [cljs.core.async.macros :refer [go]])
  (:require
   [clojure.walk :as walk]
   [cljs.core.async :as async]
   [webtorrent-cljs.util :refer [with-callback]]))

(enable-console-print!)

(defn create
  "See https://webtorrent.io/docs.  Returns a new torrent client"
  ([] (create {}))
  ([opts]
   (js/WebTorrent. (clj->js opts))))

(defn webrtc-support?
  "See https://webtorrent.io/docs.
  Returns true if webrtc is natively supported."
  []
  (.-WEBRTC_SUPPORT js/WebTorrent))

;; client.add(torrentId, [opts], [function ontorrent (torrent) {}])
(defn add
  "See https://webtorrent.io/docs.  Add a torrent.
   Returns a channel which, when read, will return the torrent."
  ([client id] (add client id {}))
  ([client id opts]
   (let [opts* (merge {:path "/tmp/webtorrent"
                       :maxWebConns 4}
                      opts)]
     (with-callback
       #(.add client id (clj->js opts*) %)))))

;; client.remove(torrentId, [function callback (err) {}])
(defn remove
  "See https://webtorrent.io/docs.  Remove a torrent.
   Returns a channel which will be written to when all file data is removed."
  [client id]
  (with-callback
    #(.remove client id %)))

;; client.destroy([function callback (err) {}])
(defn destroy
  "See https://webtorrent.io/docs.  Destroy the client.
   Returns a channel which will be written to when graceful shutdown completes."
  [client]
  (with-callback
    #(.destroy client %)))

(defn get
  "See https://webtorrent.io/docs.  Get a torrent by id."
  [client id]
  (.get id))

;; client.on('torrent', function (torrent) {})
(defn on
  "See https://webtorrent.io/docs.
  Returns a channel which is written to when a torrent is ready
  to be used (i.e. metadata is available and store is ready)."
  [client]
  (with-callback
    #(.on client "torrent" %)))

(defn info
  "See https://webtorrent.io/docs.  Get client info."
  [client]
  {:download-speed (aget client "downloadSpeed")
   :upload-speed (aget client "uploadSpeed")
   :ratio (aget client "ratio")})

;; client.seed(input, [opts], [function ontorrent (torrent) {}])
(defn seed
  "See https://webtorrent.io/docs.
  Returns a channel which is written to when the client has begun
  seeding."
  ([client input] (seed client input {}))
  ([client input opts]
   (let [opts* (merge {:path "/tmp/webtorrent"
                       :maxWebConns 4}
                      opts)]
     (with-callback
       #(.seed client input (clj->js opts*) %)))))

(defn torrents
  "See https://webtorrent.io/docs.  Get a seq of torrents."
  [client]
  (js->clj (aget client "torrents")))
