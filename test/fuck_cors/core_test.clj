(ns fuck-cors.core-test
  (:require [clojure.test :refer [deftest testing is]]
            [fuck-cors.core :refer [wrap-open-cors]]
            [clojure.string :as string]))

(def host-from-req (ns-resolve 'fuck-cors.core 'host-from-req))

(deftest test-host-from-req
  (testing "Test the get host from request"
    (let [request {:headers {"host" "yannesposito.com"}
                   :scheme :http}]
      (is (= "http://yannesposito.com"
             (host-from-req request))))))

(deftest wrap-open-cors-test
  (testing "Can use any header"
    (let [request-1
          {:server-port 443
           :server-name "yannesposito.com"
           :remote-addr "127.0.0.1"
           :uri "https://yannesposito.com/about/"
           :scheme :https
           :request-method :post
           :headers {"host" "yannesposito.com"
                     "authorization" "Bearer 1337"
                     "Content-Type" "application/json; utf-8"}
           :body "{\"foo\":\"bar\"}"}

          handler
          (fn [_]
            {:status 200
             :headers {"Origin" "https://yannesposito.com"
                       "Content-Type" "application/json; utf-8"
                       "X-SPECIFIC-HEADER" "42"}
             :body "{\"foo\":\"bar\"}"})

          wrapped (wrap-open-cors handler)
          response-1 (wrapped request-1)
          response-allowed-headers (some-> (get-in response-1 [:headers "Access-Control-Allow-Headers"])
                                           (string/split #",")
                                           (set))
          response-expose-headers (some-> (get-in response-1 [:headers "Access-Control-Allow-Headers"])
                                           (string/split #",")
                                           (set))]

      (is (contains? response-allowed-headers "Origin")
          "Should contain the Origin header")
      (is (contains? response-allowed-headers "X-SPECIFIC-HEADER")
          "Can contain any strange custom made headers returned by the response")

      (is (contains? response-expose-headers "Origin")
          "Should contain the Origin header")
      (is (contains? response-expose-headers "X-SPECIFIC-HEADER")
          "Can contain any strange custom made headers returned by the response")

      ;; full response for example purpose
      (is (= {:status 200
              :headers {"Origin" "https://yannesposito.com"
                        "Content-Type" "application/json; utf-8"
                        "X-SPECIFIC-HEADER" "42"
                        "Access-Control-Allow-Origin" "https://yannesposito.com"
                        "Access-Control-Allow-Headers" "Origin,Content-Type,X-SPECIFIC-HEADER"
                        "Access-Control-Allow-Methods" "HEAD, GET, PATCH, POST, CONNECT, PUT, DELETE, OPTIONS, TRACE"
                        "Access-Control-Allow-Credentials" "true"
                        "Access-Control-Expose-Headers" "Origin,Content-Type,X-SPECIFIC-HEADER"}
              :body "{\"foo\":\"bar\"}"}
             response-1)))))
