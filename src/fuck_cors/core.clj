(ns fuck-cors.core
  (:require [clojure.string :as string]))

(defn- host-from-req
  [request]
  (str (-> request :scheme name)
       "://"
       (get-in request [:headers "host"])))

(defn- get-header
  [request header-name]
  (let [rawref (get-in request [:headers header-name])]
    (if rawref
        (string/replace rawref #"(http://[^/]*).*$" "$1")
        nil)))

(defn wrap-open-cors
  "Open your Origin Policy to Everybody, no limit"
  [handler]
  (fn [request]
    (let [origin  (get-header request "origin")
          referer (get-header request "referer")
          host    (host-from-req request)
          origins (if origin
                    origin
                    (if referer
                      referer
                      host))
          {:keys [headers] :as original-response} (handler request)
          resp-cors-headers
          {"Access-Control-Allow-Origin" origins
           "Access-Control-Allow-Headers" (string/join "," (keys headers))
           "Access-Control-Allow-Methods" "HEAD, GET, PATCH, POST, CONNECT, PUT, DELETE, OPTIONS, TRACE"
           "Access-Control-Allow-Credentials" "true"
           "Access-Control-Expose-Headers" (string/join "," (keys headers))}]
      (-> original-response
          (update-in [:headers] #(into % resp-cors-headers))))))

(defn wrap-preflight
  "Add a preflight answer. Will break any OPTIONS handler, beware.
  To put AFTER wrap-open-cors"
  [handler]
  (fn [request]
    (if (= (request :request-method) :options)
      (into request {:status 200 :body "preflight complete"})
      (handler request))))


