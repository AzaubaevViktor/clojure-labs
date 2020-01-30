(ns labs.6)

(defn _integrated_seq [f step]
  (let [calculate (fn [x] (* (+ (f x) (f (+ x step))) (/ step 2)) )]
    (iterate
      (fn [[x, value]]
        (let [next_step (+ x step)]
          (list
            next_step
            (+ value (calculate next_step))
            ))
        )
      (list 0 (calculate 0))
      ))
  )

(def integrated_seq (memoize _integrated_seq))

(defn integrate
  ([f max_x] (integrate f max_x 0.01))
  ([f max_x step]                                           ; expand sequence
   (nth (let [steps (- (/ max_x step) 1)]
          (nth (integrated_seq f step) steps)
          ) 1)
   )
  )


