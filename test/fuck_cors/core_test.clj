(ns fuck-cors.core-test
  (:require [clojure.test :refer :all]
            [fuck-cors.core :refer :all]))

(def host-from-req (ns-resolve 'fuck-cors.core 'host-from-req))

(deftest test-host-from-req
  (testing "Test the get host from request"
    (let [request {:headers {"host" "yannesposito.com"}
                   :scheme :http}]
      (is (= "http://yannesposito.com"
             (host-from-req request))))))
