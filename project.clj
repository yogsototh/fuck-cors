(defproject fuck-cors "0.1.8"
  :description "Fuck CORS and open your API to everyone"
  :url "https://git.esy.fun/yogsototh/fuck-cors"
  :license {:name "MIT"
            :url "http://opensource.org/licences/MIT"}
  :deploy-repositories [["releases" {:url "https://clojars.org/repo" :creds :gpg}]
                        ["snapshots" {:url "https://clojars.org/repo" :creds :gpg}]]
  :dependencies [[org.clojure/clojure "1.11.4"]])
