(ns webtorrent-cljs.test-runner
  (:require
   [doo.runner :refer-macros [doo-tests]]
   [webtorrent-cljs.core-test]))

(enable-console-print!)

(doo-tests 'webtorrent-cljs.core-test)
