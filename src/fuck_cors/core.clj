(ns fuck-cors.core)

(defn- host-from-req
  [request]
  (str (-> request :scheme name)
       "://"
       (get-in request [:headers "host"])))

(defn- get-header
  [request header-name]
  (let [rawref (get-in request [:headers header-name])]
    (if rawref
        (clojure.string/replace rawref #"(http://[^/]*).*$" "$1")
        nil)))

(defn wrap-open-cors
  "Open your Origin Policy to Everybody, no limit"
  [handler]
  (fn [request]
    (let [origin (get-header request "origin")
          referer (get-header request "referer")
          host (host-from-req request)
          origins (if origin
                    origin
                    (if referer
                      referer
                      host))
          headers {"Access-Control-Allow-Origin" origins
                   "Access-Control-Allow-Headers" "Origin, X-Requested-With, Content-Type, Accept, Cache-Control, Accept-Language, Accept-Encoding, Authorization"
                   "Access-Control-Allow-Methods" "HEAD, GET, POST, PUT, DELETE, OPTIONS, TRACE"
                   "Access-Control-Allow-Credentials" "true"
                   "Access-Control-Expose-Headers" "content-length"
                   "Vary" "Accept-Encoding, Origin, Accept-Language"}]
      (-> (handler request)
          (update-in [:headers] #(into % headers))))))
