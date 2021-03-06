(ns minijava.test-flow
  (:use [minijava gas flow] clojure.test)
  (:require [minijava.temp :as tm])
  (:import [minijava.gas cmpl jcc jmp LABEL]))

(deftest test-lookup-lbl
  (tm/reset-num!)
  (let [t (tm/label)
        f (tm/label)
        a (tm/temp)
        b (tm/temp)
        other (tm/label)
        done (tm/label "done")
        prog (vector
              (LABEL. other)
              (cmpl. a b)
              (jcc. := t)
              (jmp. f)
              (LABEL. t)
              (jmp. other)
              (LABEL. f))]
    (is (= (lookup-lbl prog)
           (do
             (tm/reset-num!)
             {f 6, t 4, other 0, done 7})))))

(deftest test-flow
  (tm/reset-num!)
  (let [t (tm/label)
        f (tm/label)
        a (tm/temp)
        b (tm/temp)
        other (tm/label)
        prog (vector
              (LABEL. other)
              (cmpl. a b)
              (jcc. := t)
              (jmp. f)
              (LABEL. t)
              (jmp. other)
              (LABEL. f))]
    (is (= (flow prog)
           [[1] [2] [3 4] [6] [5] [0] [7]]))))
