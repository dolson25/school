#lang plai

(define-type Binding
  [binding (name symbol?) (named-expr CFWAE?)])
 
(define-type CFWAE
  [num (n number?)]
  [binop (op procedure?) (lhs CFWAE?) (rhs CFWAE?)]
  [with (lob (listof Binding?)) (body CFWAE?)]
  [id (name symbol?)]
  [if0 (c CFWAE?) (t CFWAE?) (e CFWAE?)]
  [fun (args (listof symbol?)) (body CFWAE?)]
  [app (f CFWAE?) (args (listof CFWAE?))])
 
(define-type Env
  [mtEnv]
  [anEnv (name symbol?) (value CFWAE-Value?) (env Env?)])
 
(define-type CFWAE-Value
  [numV (n number?)]
  [closureV (params (listof symbol?))
            (body CFWAE?)
            (env Env?)])

(define op-table
  (list (list '+ +)
        (list '- -)
        (list '* *)
        (list '/ /)))


;lookup-op? : symbol -> procedure or false
;given a symbol, if it represents one of our four operators, return
;             the procedure associated with it, else return false
(define (lookup-op op)
  (if (assoc op op-table) (first (cdr (assoc op op-table))) #f))

(test (lookup-op '+) +)
(test (lookup-op '-) -)
(test (lookup-op '*) *)
(test (lookup-op '/) /)
(test (lookup-op 'a) #f)
(test (lookup-op -) #f)
(test (lookup-op #f) #f)

 
; parse : expression -> CFWAE
; This procedure parses an expression into a CFWAE
(define (parse s-exp)
  (cond
    [(number? s-exp) (num s-exp)]
    [(symbol? s-exp)
       (cond
          [(lookup-op s-exp) (error "Illegal syntax")]
          [(equal? s-exp 'with) (error "Illegal syntax")]
          [(equal? s-exp 'if0) (error "Illegal syntax")]
          [(equal? s-exp 'fun) (error "Illegal syntax")]
          [else (id s-exp)])]
    [(list? s-exp)
     (cond
       [(empty? s-exp) (error "Illegal syntax")]
       [(equal? (first s-exp) 'with)
        (cond
          [(not (list? (second s-exp))) (error "Illegal syntax")]
          [(not (equal? (length s-exp) 3)) (error "Illegal syntax")]
          [else
             (if (andmap (lambda (x)
                        (cond
                          [(not (list? x)) #f]
                          [(not (equal? (length x) 2)) #f]
                          [(not (symbol? (first x))) #f]
                          [else  (binding (first x)
                                  (parse (second x)))]))(second s-exp))
                 (with (map (lambda (x)
                        (if (equal? (length x) 2)
                               (if (and (not (lookup-op (first x)))
                                        (not (equal? 'with (first x)))
                                        (not (equal? 'if0 (first x)))
                                        (not (equal? 'fun (first x))))
                                 (binding (first x)
                                 (parse (second x)))
                                 (error "Illegal syntax"))
                                  #f)) (second s-exp))
                       (parse (third s-exp)))
                 (error "Illegal syntax"))])]
       [(equal? (first s-exp) 'fun)
        (cond
          [(not (list? (second s-exp))) (error "Illegal syntax")]
          [(not (equal? (length s-exp) 3)) (error "Illegal syntax")]
          [else
             (if (andmap (lambda (x)
                        (cond
                          [(not (symbol? x)) #f]
                          [else  (id x)]))(second s-exp))
                 (fun (map (lambda (x)
                            (if (and (not (lookup-op x))
                                        (not (equal? 'with x))
                                        (not (equal? 'if0 x))
                                        (not (equal? 'fun x)))
                                 x
                               (error "Illegal syntax")))
                           (second s-exp))
                       (parse (third s-exp)))
                 (error "Illegal syntax"))])]
       [(equal? (first s-exp) 'if0)
                 (if (equal? (length s-exp) 4)
                              (if0 (parse (second s-exp))
                                   (parse (third s-exp))
                                   (parse (fourth s-exp)))
                              (error "Illegal syntax"))]
       [(equal? (first s-exp) 'app)
            (cond
          [(not (list? (third s-exp))) (error "Illegal syntax")]
          [(not (equal? (length s-exp) 3)) (error "Illegal syntax")]
          [else
                 (app (parse (second s-exp))
                  (map (lambda (x)
                           (parse x)) (third s-exp)))])]
       [else
          (cond
            [(equal? (length s-exp) 3) 
                   (binop (lookup-op (first s-exp))
                      (parse (second s-exp))
                      (parse (third s-exp)))]
            [else (error "Illegal syntax")])])]
    [else (error "Illegal syntax")]))


;Feature: literals
(test (parse '5) (num 5))
(test/exn (parse "hello") "Illegal syntax")
(test/exn (parse true) "Illegal syntax")

;Feature: binary operators
(test (parse '(+ 1 2)) (binop + (num 1) (num 2)))
(test (parse '(- 1 2)) (binop - (num 1) (num 2)))
(test (parse '(* 1 2)) (binop * (num 1) (num 2)))
(test (parse '(/ 1 2)) (binop / (num 1) (num 2)))
(test/exn (parse '(+ 5)) "Illegal syntax")
(test/exn (parse '(+ 1 2 3)) "Illegal syntax")

;Feature: id
(test (parse 'a) (id 'a))
(test/exn (parse '+) "Illegal syntax")
(test/exn (parse '-) "Illegal syntax")
(test/exn (parse '*) "Illegal syntax")
(test/exn (parse '/) "Illegal syntax")
(test/exn (parse 'with) "Illegal syntax")
(test/exn (parse 'if0) "Illegal syntax")
(test/exn (parse 'fun) "Illegal syntax")

;Feature: if0
(test (parse '(if0 (+ 5 6) 5 7))
              (if0 (binop + (num 5) (num 6)) (num 5) (num 7)))
(test/exn (parse '(if0 (+ 1 2) 4)) "Illegal syntax")
(test/exn (parse '(if0 (+ 1 2) 4 5 6)) "Illegal syntax")

 ;Feature: with
(test (parse '(with ([x 1] [x 2]) (+ x x)))
      (with (list (binding 'x (num 1))
                  (binding 'x (num 2)))
            (binop + (id 'x) (id 'x))))
(test/exn (parse '(with ([x 5]))) "Illegal syntax")
(test/exn (parse '(with ([x 5]) (+ 1 x) (+ 2 x))) "Illegal syntax")
(test/exn (parse '(with x (+ 1 x) (+ 2 x))) "Illegal syntax")
(test/exn (parse '(with (x 5) (+ 1 x) (+ 2 x))) "Illegal syntax") 
(test/exn (parse '(with ([x]) (+ 1 x))) "Illegal syntax")
(test/exn (parse '(with ([x 5 6]) (+ 1 x))) "Illegal syntax")
(test/exn (parse '(with ([42 5]) (+ 1 2))) "Illegal syntax")
(test/exn (parse '(with ([+ 5]) (+ 1 2))) "Illegal syntax")
(test/exn (parse '(with ([with 5]) (+ 1 2))) "Illegal syntax")
(test/exn (parse '(with ([fun 5]) (+ 1 2))) "Illegal syntax")
(test/exn (parse '(with ([if0 5]) (+ 1 2))) "Illegal syntax")
 ;* Is there a test case for: invalid binding (duplicated id)?
;(test/exn (parse '(with ([x 1] [x 2]) x)) "Illegal syntax")

 ;Feature: fun
(test (parse '(fun (x y z) (+ y z))) (fun (list 'x 'y 'z)
                                         (binop + (id 'y) (id 'z))))       
(test/exn (parse '(fun (x y))) "Illegal syntax")
(test/exn (parse '(fun (x y) (z) (+ 5 z))) "Illegal syntax")
(test/exn (parse '(fun x (+ x 2))) "Illegal syntax")
(test/exn (parse '(fun (42 5) (+ 1 2))) "Illegal syntax")
(test/exn (parse '(fun (y -) (+ y 2))) "Illegal syntax")
 ;* Is there a test case for: invalid parameter (duplicated id)?
;(test/exn (parse '(fun (x x) x)) "Illegal syntax")

;Feature: app
(test (parse '(app (fun (x) (+ x 1)) (4)))
      (app (fun (list 'x) (binop + (id 'x) (num 1))) (list (num 4))))

;Other:
(test/exn (parse '()) "Illegal syntax")


;; num-zero? : RCFAE-Value -> boolean
(define (num-zero? n)
  (zero? (numV-n n)))

;; num-op : numV numV -> numV
;; ops two numV number representations
(define (num-op op n1 n2)
  (cond
    [(or (not (numV? n1)) (not (numV? n2)))
                     (error "failed arithmetic...not a number")]
    [(and (eq? op /) (zero? (numV-n n2))) (error "division by zero")]
    [else (numV (op (numV-n n1) (numV-n n2)))]))

;; lookup : symbol Env -> FWAE-Value
;; looks up an identifier in an environment and returns the value
;; bound to it (or reports error if not found)
(define (lookup name env)
  (type-case Env env
    [mtEnv () (error 'lookup "free identifier")]
    [anEnv (bound-name bound-value rest-env)
           (if (symbol=? bound-name name)
               bound-value
               (lookup name rest-env))]))

; extend-Env : list(binding) Env -> Env
; extends an environment by pointing the symbols in the
        ;list to their respective expressions
(define (extend-Env lob env)
        (foldl (lambda (x env)
           (anEnv (binding-name x)
                  (interp (binding-named-expr x) env) env)) env lob)) 


; interp : CFWAE Env -> CFWAE-Value
; This procedure interprets the given CFWAE in the environment
; and produces a result in the form of a CFWAE-Value
(define (interp expr env)
  (type-case CFWAE expr
    [num (n) (numV n)]
    [binop (op l r) (num-op op (interp l env) (interp r env))]
    [id (v) (lookup v env)]
    [with (lob  body)   (interp body (extend-Env lob env))]
    [if0 (test truth falsity)
       (if (not (numV? (interp test env)))
           (error "If0 test not a number")
        (if (num-zero? (interp test env))
          (interp truth env)
          (interp falsity env)))]
    [fun (args body)
         (closureV args body env)]
    [app (f args) 
         (cond
           [(not (fun? f)) (error "app expression not a function")]
           [(> (length (fun-args f)) (length args))
                                         (error "too few args")]
           [(< (length (fun-args f)) (length args))
                                         (error "too many args")]
           [else 1])]))


;Feature: literals
(test (interp (parse '5) (mtEnv)) (numV 5))

;Feature: binary operators
(test (interp (parse '(+ 4 1)) (mtEnv)) (numV 5))
(test/exn (interp (parse '(+ 1 (fun (x) (+ x x))) )(mtEnv))
          "failed arithmetic...not a number")
(test/exn (interp (parse '(+ (fun (x) (+ x x)) 1) )(mtEnv))
          "failed arithmetic...not a number")
(test (interp (parse '(- 4 1)) (mtEnv)) (numV 3))
(test/exn (interp (parse '(- 1 (fun (x) (+ x x))) )(mtEnv))
          "failed arithmetic...not a number")
(test/exn (interp (parse '(- (fun (x) (+ x x)) 1) )(mtEnv))
          "failed arithmetic...not a number")
(test (interp (parse '(* 4 1)) (mtEnv)) (numV 4))
(test/exn (interp (parse '(* 1 (fun (x) (+ x x))) )(mtEnv))
          "failed arithmetic...not a number")
(test/exn (interp (parse '(* (fun (x) (+ x x)) 1) )(mtEnv))
          "failed arithmetic...not a number")
(test (interp (parse '(/ 4 2)) (mtEnv)) (numV 2))
(test/exn (interp (parse '(/ 1 (fun (x) (+ x x))) )(mtEnv))
          "failed arithmetic...not a number")
(test/exn (interp (parse '(/ (fun (x) (+ x x)) 1) )(mtEnv))
          "failed arithmetic...not a number")
(test/exn (interp (parse '(/ 4 0)) (mtEnv)) "division by zero")

;Feature: id
(test/exn (interp (parse 'x) (mtEnv)) "free identifier")
(test/exn (interp (parse '(+ 4 w)) (mtEnv)) "free identifier")

;Feature: if0
(test (interp (parse '(if0 (- 5 5) 4 5)) (mtEnv)) (numV 4))
(test (interp (parse '(if0 (+ 5 5) 4 5)) (mtEnv)) (numV 5))
(test/exn (interp (parse '(if0 (fun (x) (+ x x)) 4 5)) (mtEnv))
                                      "If0 test not a number")

;Feature: with
(test (interp (parse '(with ([x 5]) x)) (mtEnv)) (numV 5))
(test (interp (parse '(with ([x 5] [y 6]) (+ x y))) (mtEnv)) (numV 11))
(test (interp (parse '(with ([x 5]) (+ x 5))) (mtEnv)) (numV 10))
(test (interp (parse '(with ([x 5]) (with ([x 6]) (+ x 5)))) (mtEnv))
                                                           (numV 11))
(test (interp (parse '(with ([x 5]) (with ([x (+ x 1)]) (+ x 5))))
                                                 (mtEnv))  (numV 11))

;Feature: fun
(test (interp (parse '(fun (x y z) (+ x y))) (mtEnv))
      (closureV (list 'x 'y 'z) (binop + (id 'x) (id 'y)) (mtEnv)))
(test (interp (parse '(with ([x 5]) (fun (y) (+ x y)))) (mtEnv))
           (closureV (list 'y) (binop + (id 'x) (id 'y))
            (anEnv  'x (numV 5) (mtEnv))))


;Feature: app
 ;* Is there a working app test case?
;(test (interp (parse '(app (fun (x) (+ x 1)) 6)) (mtEnv)) (numV 7))
(test/exn (interp (parse '(app 5 (3 4))) (mtEnv))
                        "app expression not a function")
(test/exn (interp (parse '(app (fun (x y) (+ x y)) (1))) (mtEnv))
          "too few args")
(test/exn (interp (parse '(app (fun (x) (+ x 1)) (1 2))) (mtEnv))
          "too many args")
 ;* Is there an app (static, not dynamic scope) case test?
;(with ((f (with ([x 5])`
;`;    (fun (y) (+ x y))   ) ))`
;`    (with (( x 10)))  ; <-- this with should not change the static x defined above`
;`    (f 6))`










