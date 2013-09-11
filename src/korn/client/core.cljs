(ns korn.client.core
  (:require [domina :refer [by-id value set-html!]]
            [domina.events :refer [listen!]]
            [shoreleave.remotes.http-rpc :refer [remote-callback]]))



(defn- load-remote
  "Load a remote callback into a content div and run provided init function"
  ([callback] (remote-callback callback [] #(set-html! (by-id "container") %)))
  ([callback init] (remote-callback callback [] #(do (set-html! (by-id "container") %)(init)))))

;; TEST
(defn test-init
  []
  (listen! (by-id "test-button") :click (fn [] (js/alert "test"))))

(defn replace-content-test
  []
  (load-remote :api/testapi test-init))

;; HOME
(defn home-init
  []
  (listen! (by-id "home") :click (fn [] (home)))
  (listen! (by-id "test-button") :click (fn [] (replace-content-test))))

(defn home
  []
  (load-remote :api/home home-init))

  
(defn ^:export init
  []
  (home-init))
