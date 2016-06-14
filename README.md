# webtorrent-cljs

## Usage

In your project.clj, add to your `:dependencies`:

    [webtorrent-cljs "0.1.0"]

A trivial example:

    (ns example
      (:require-macros
       [cljs.core.async.macros :refer [go]])
      (:require 
        [cljs.core.async :as async]
        [webtorrent-cljs.torrent :as wtt]
        [webtorrent-cljs.file :as wtf] ;; har har
        [webtorrent-cljs.client :as wtc]))
  
    (def client (wtc/create))

    (let [buf (js/Buffer. (str "content" (rand)))
          c1 (wtc/seed client buf)]
      (go
        (let [[t] (async/<! c1)]
          (prn (wtt/info t))
          (doseq [file (js->clj (aget t "files"))]
            (let [c (wtf/get-buffer file)
                  [err buf] (async/<! c)]
              (prn "got" buf))))))


## Development

Open a terminal and type `lein repl` to start a Clojure REPL
(interactive prompt).

In the REPL, type

```clojure
(run)
(browser-repl)
```

The call to `(run)` starts the Figwheel server at port 3449, which takes care of
live reloading ClojureScript code and CSS. Figwheel's server will also act as
your app server, so requests are correctly forwarded to the http-handler you
define.

Running `(browser-repl)` starts the Figwheel ClojureScript REPL. Evaluating
expressions here will only work once you've loaded the page, so the browser can
connect to Figwheel.

When you see the line `Successfully compiled "resources/public/app.js" in 21.36
seconds.`, you're ready to go. Browse to `http://localhost:3449` and enjoy.

**Attention: It is not needed to run `lein figwheel` separately. Instead we
launch Figwheel directly from the REPL**

## Testing

To run the Clojure tests, use

``` shell
lein test
```

To run the Clojurescript you use [doo](https://github.com/bensu/doo). This can
run your tests against a variety of JavaScript implementations, but in the
browser and "headless". For example, to test with PhantomJS, use

``` shell
lein doo phantom
```

## License

Copyright © 2016 Colin Steele

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.

## Chestnut

Created with [Chestnut](http://plexus.github.io/chestnut/) 0.13.0 (31005e85).
