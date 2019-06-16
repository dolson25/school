; David Olson
; Jonathan Francis
#lang lazy

; Macro for test case display
(define print-only-errors #f)

;(test l r) → output to screen
;  l : any/c
;  r : any/c
; Testing facility for lazy language
(define (test l r)
  (if (equal? l r)
      (if print-only-errors
          ()
          (printf "Test Passed~n"))
      (printf
       "Test Failed.~nActual:   ~S ~nExpected: ~S~n"
       l r)))

;(take-while p l) → (listof any/c)
;  p : (any/c . -> . boolean?)
;  l : (listof any/c)
; Returns the prefix of l such that for all elements p
; returns true. 
(define (take-while p l)
  (if (or (empty? l) (not (p (first l))))
      empty (cons (first l) (take-while p (rest l)))))

;(nums n) → (listof exact-nonnegative-integer?)
;  n : exact-nonnegative-integer?
; Lazily constructs the infinite list such that
; the first number is n and every subsequent number
; is one more than the last number
(define (nums n)
  (cons n (nums (+ n 1))))

;(build-infinite-list f) → (listof any/c)
;  f : (exact-nonnegative-integer? . -> . any/c)
; Lazily constructs the infinite list such that
; (list-ref (build-infinite-list f) i) returns (f i).
(define (build-infinite-list f)
  (map f (nums 0)))

;>1 : (listof exact-positive-integer?)
; The list of all numbers greater than 1
(define >1 (build-infinite-list (lambda (x) (+ x 2))))

;(notdivby? a b) → boolean?
;  a : exact-nonnegative-integer?
;  b : exact-nonnegative-integer?
; Returns true if a is not divisable by b.
(define (notdivby? a b)
  (or (= b 0) (not (= (modulo a b) 0))))

;(prime? n) → boolean?
;  n : exact-positive-integer?
; Returns true if n is prime.
(define (prime? n)
  (andmap
   (lambda (x) (notdivby? n x))
   (take-while (lambda (x) (< x n)) >1)))

;primes : (listof exact-positive-integer?)
; The list of all primes.
(define primes
  (filter prime? >1))

;(prime?/fast n) → boolean
;  n : exact-positive-integer?
; Returns true if n is prime, but tests only prime
; factors from primes/fast.
(define (prime?/fast n)
  (if (= n 2) #true
      (andmap
       (lambda (x) (notdivby? n x))
       (take-while
        (lambda (x) (<= x (sqrt n)))
        primes/fast))))

;primes/fast : (listof exact-positive-integer?)
; The list of all primes constructed with prime?/fast.
(define primes/fast (filter prime?/fast >1))


;build-vector? : num, fun -> vector
;helper for build table
(define (build-vector num f)
  (apply vector (build-list num f)))


;build-table? : exact-positive-integer, exact-positive-integer, func ->
                               ;vector( vector ( any/c))
;Lazily constructs a vector such that
    ;(vector-ref (vector-ref (build-table rows cols f) i) j) equals
    ;(f i j), when (< i rows) (< j cols).
(define (build-table rows cols f)
      (build-vector rows (lambda (x)
                      (build-vector cols (lambda (y) (f x y))))))

;table-ref? : vector(vector(int)), int, int -> integer
;helper for lcs-length, returns value of table at row,col
(define (table-ref t r c)
  (vector-ref (vector-ref t r) c))

;lcs-length? : string, string -> exact-nonnegative-integer
;Computes the length of the longest common subsequence of two strings
(define (lcs-length s1 s2)
 (letrec ([t (build-table
         (+ 1 (string-length s1)) (+ 1 (string-length s2))
            (lambda (x y)
              (if (or (equal? x 0) (equal? y 0))
                  0
                 (if (char=? (string-ref s1 (- x 1))
                             (string-ref s2 (- y 1)))
                     (+ 1 (table-ref t (- x 1) (- y 1)))
                     (max (table-ref t (- x 1) y)
                          (table-ref t x (- y 1)))))))])
  (table-ref t (string-length s1) (string-length s2))))


; Test cases
(test (take-while odd? '())
      '())
(test (take-while odd? (list 1 3 4))
      (list 1 3))
(test (take-while
       (lambda (n) (< n 5))
       (list 1 2 3 4 5 1 2))
      (list 1 2 3 4))
(test (take-while
       (lambda (x) (< x 100))
       (build-infinite-list
        (lambda (x) (* x x))))
      (list 0 1 4 9 16 25 36 49 64 81))
(test (list-ref (build-infinite-list
                 (lambda (x) (* x x))) 3) 9)
(test (list-ref (build-infinite-list
                 (lambda (x) (* x x))) 4) 16)
(test (take 10 (build-infinite-list
                 (lambda (x) (* x x))))
        (list 0 1 4 9 16 25 36 49 64 81))
(test (prime? 83) #true)
(test (prime? 91) #false)
(test (prime? 89003) #true)
(test (prime? 89007) #false)
(test (take 10 primes)
      (list 2 3 5 7 11 13 17 19 23 29))
(test (take-while (lambda (x) (< x 30)) primes)
      (list 2 3 5 7 11 13 17 19 23 29))
(test (take-while (lambda (x) (< x 100)) primes)
      (list 2 3 5 7 11 13 17 19 23 29 31 37 41 43 47
            53 59 61 67 71 73 79 83 89 97))
(test (prime?/fast 83) (prime? 83))
(test (prime?/fast 91) (prime? 91))
(test (prime?/fast 89003) #true)
(test (prime?/fast 89007) #false)
(test (take 10 primes/fast)
      (take 10 primes))
(test (take-while (lambda (x) (< x 30)) primes/fast)
      (list 2 3 5 7 11 13 17 19 23 29))
(test (take-while (lambda (x) (< x 100)) primes/fast)
      (list 2 3 5 7 11 13 17 19 23 29 31 37 41 43 47
            53 59 61 67 71 73 79 83 89 97))
(test (build-table 2 2 (lambda (x y) (+ x y)))
      '#(#(0 1) #(1 2)))
(test (build-table 2 2 (lambda (x y) (* x y)))
      '#(#(0 0) #(0 1)))
(test (lcs-length "yo" "yea") 1)
(test (lcs-length "artist" "artsy") 4)
(test (lcs-length "David Olson"
                  "Jonathan Francis") 3)