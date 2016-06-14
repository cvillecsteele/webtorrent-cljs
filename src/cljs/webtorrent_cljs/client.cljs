(ns webtorrent-cljs.client
  (:refer-clojure :exclude [remove get])
  (:require-macros
   [cljs.core.async.macros :refer [go]])
  (:require
   [clojure.walk :as walk]
   [cljs.core.async :as async]
   [webtorrent-cljs.util :refer [with-callback]]
   [webtorrent]
   [buffer]))

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





(def client (create))

;; (let [buf (buffer/Buffer. (str "content" (rand)))
;;       c1 (seed client buf)]
;;   (go
;;     (let [t (async/<! c1)]
;;       (doseq [f (js->clj (aget t "files"))]
;;         (.log js/console "file" f)
;;         (.getBuffer f (fn [err buf]
;;                         (.log js/console (.toString buf)))))
;;       (.log js/console t)
;;       (prn "infohash" (aget t "infoHash")))))

(let [ih "b1ad70feb18d6fa50e068bcf243d5978b69b92a8" ;; "magnet:?xt=urn:btih:b1ad70feb18d6fa50e068bcf243d5978b69b92a8&dn=Logo.idraw&tr=udp%3A%2F%2Fexodus.desync.com%3A6969&tr=udp%3A%2F%2Ftracker.coppersurfer.tk%3A6969&tr=udp%3A%2F%2Ftracker.internetwarriors.net%3A1337&tr=udp%3A%2F%2Ftracker.leechers-paradise.org%3A6969&tr=udp%3A%2F%2Ftracker.openbittorrent.com%3A80&tr=wss%3A%2F%2Ftracker.btorrent.xyz&tr=wss%3A%2F%2Ftracker.fastcast.nz&tr=wss%3A%2F%2Ftracker.openwebtorrent.com&tr=wss%3A%2F%2Ftracker.webtorrent.io"
      c (add client ih)]
  (doseq [t (js->clj (aget client "torrents"))]
    (.log js/console "torrent" t))
  (go
    (let [t (async/<! c)]
      (doseq [f (js->clj (aget t "files"))]
        (.log js/console "add file" f)
        (.getBuffer f (fn [err buf]
                        (.log js/console "d/l" (.toString buf)))))
      (.log js/console t)
      (prn (aget t "infoHash")))))


