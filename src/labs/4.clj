(ns labs.4)

(defn gen_2_seq [custom, source]
  (for [x custom y source :when  (not (.contains x y)) ]
    (conj x y))
  )


(defn gen_sequence [_source, number]
  (let [source (distinct (into-array _source))]
    (println source)
    (cond
      (< number 1) []
      :else (let [start (for [x source] (list x))]
              (println start)
              (reduce gen_2_seq start (repeat (dec number) source))
              )
      )
    )
  )


(println (gen_sequence "test" 2))
