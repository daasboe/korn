(ns korn.client.core
  (:use [cljs.reader :only (read-string)]
        [korn.client.routes :only (nav)])
  (:require [enfocus.core :as ef]
            [enfocus.events :as ev]
            [shoreleave.browser.history :refer [navigate-callback history set-token! replace-token! get-token]]
            [shoreleave.remotes.http-rpc :refer [remote-callback]]
            [korn.client.recipe :refer [recipe-index-page]]
            [korn.validations :as v])
  (:require-macros [enfocus.macros :as em]))

(declare setup
         navigate
         add-to-content-pane
         start)


(em/defaction setup []
  "#home" (ev/listen :click #(do
                               (.preventDefault %)
                               (navigate :home)))
  "#new" (ev/listen :click #(do
                              (.preventDefault %)
                              (navigate :new)))
  "#about" (ev/listen :click #(do
                                (.preventDefault %)
                                (navigate :about))))

(defn navigate "Navigate to page represented by token" [token]
  (set-token! history (clojure.string/replace (str token) #":" "")))

;;;;;;;;;;
;; Pages


(em/defaction add-to-content-pane [content]
  "#content-pane" (ef/append content))

;;;;;;;;;;
;; Init
(defn start []
  (do 
    (navigate-callback (fn [m] (nav (m :token ))))
    (let  [token (get-token history)]
      (if (empty? token)
        (navigate :home)
        (nav (keyword token))))
    (setup)))

(set! (.-onload js/window) start)
