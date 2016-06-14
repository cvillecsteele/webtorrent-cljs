(ns webtorrent-cljs.core
  (:require-macros
   [cljs.core.async.macros :refer [go]])
  (:require
   [clojure.walk :as walk]
   [cljs.core.async :as async]
   [webtorrent-cljs.util]
   [webtorrent-cljs.client :as c]
   [webtorrent-cljs.file :as f]))

(enable-console-print!)

(comment

  (def client (c/create))

  (let [buf (buffer/Buffer. (str "content" (rand)))
        c1 (seed client buf)]
    (go
      (let [t (async/<! c1)]
        (doseq [f (js->clj (aget t "files"))]
          (.log js/console "file" f)
          (.getBuffer f (fn [err buf]
                          (.log js/console (.toString buf)))))
        (.log js/console t)
        (prn "infohash" (aget t "infoHash")))))

  (let [ih  "magnet:?xt=urn:btih:b1ad70feb18d6fa50e068bcf243d5978b69b92a8&dn=Logo.idraw&tr=udp%3A%2F%2Fexodus.desync.com%3A6969&tr=udp%3A%2F%2Ftracker.coppersurfer.tk%3A6969&tr=udp%3A%2F%2Ftracker.internetwarriors.net%3A1337&tr=udp%3A%2F%2Ftracker.leechers-paradise.org%3A6969&tr=udp%3A%2F%2Ftracker.openbittorrent.com%3A80&tr=wss%3A%2F%2Ftracker.btorrent.xyz&tr=wss%3A%2F%2Ftracker.fastcast.nz&tr=wss%3A%2F%2Ftracker.openwebtorrent.com&tr=wss%3A%2F%2Ftracker.webtorrent.io" ;; "b1ad70feb18d6fa50e068bcf243d5978b69b92a8"
        c (c/add client ih)]
    (go
      (let [torrents (async/<! c)
            t (first torrents)]
        (.log js/console "torrent" t)
        (prn "infohash is" (aget t "infoHash"))
        (doseq [file (js->clj (aget t "files"))]
          (.log js/console "file" file)
          (let [c (f/get-buffer file)
                [err buf] (async/<! c)]
            (prn "got" buf))))))

  ) ;; comment
