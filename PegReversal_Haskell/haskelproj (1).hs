type Position = (Int,Int)
data Color = W | B deriving (Eq, Show)
data Peg = Peg Position Color deriving (Eq, Show)
data Move = M Position deriving (Eq, Show)
type Board = [Peg]
data State = S Move Board deriving (Eq, Show)



createBoard:: Position -> Board

createBoard (x,y) = createBoardH (x,y) [(-3,-1),(-3,0),(-3,1),(-2,-1),(-2,0),(-2,1),(-1,-3),(-1,-2),(-1,-1),(-1,0),(-1,1),(-1,2),(-1,3),(0,-3),(0,-2),(0,-1),(0,0),(0,1),(0,2),(0,3),(1,-3),(1,-2),(1,-1),(1,0),(1,1),(1,2),(1,3),(2,-1),(2,0),(2,1),(3,-1),(3,0),(3,1)]
createBoardH (x,y) [] = []
createBoardH (x,y) ((k,p):t) |  x<(-1) && (y>1 || y<(-1)) = error "The position is not valid"
							 |x>1 && (y>1 || y<(-1)) = error "The position is not valid"
                             | x==k && y==p =  Peg (k,p) W : createBoardH (x,y) t
                             |otherwise = Peg (k,p) B : createBoardH (x,y) t
							 
isValidMove:: Move -> Board -> Bool

isValidMove (M (x,y)) (Peg (p,k) v : t) |isValidMoveH (Peg (p,k) v : t) (x,y) =False
                                           |isValidMoveRight  (Peg (p,k) v : t) (x+1,y) = True
                                            |isValidMoveLeft  (Peg (p,k) v : t) (x-1,y) = True
											|isValidMoveUp  (Peg (p,k) v : t) (x,y+1) = True
											|isValidMoveDown  (Peg (p,k) v : t) (x,y-1) = True
                                            |otherwise = False
isValidMoveH  [] (j,t) = False
isValidMoveH  (Peg (p,k) v : t) (j,l) | j==p && l==k && v==W = True
                                     |otherwise = isValidMoveH  t (j,l)

isValidMoveRight  [] (j,t) = False
isValidMoveRight  (Peg (p,k) v : t) (j,l) | j==p && l==k && v==W = True
                                                   |otherwise = isValidMoveRight  t (j,l)

isValidMoveLeft  [] (j,t) = False
isValidMoveLeft  (Peg (p,k) v : t) (j,l) | j==p && l==k && v==W = True
                                                  |otherwise = isValidMoveRight   t (j,l)

isValidMoveUp [] (j,t) = False
isValidMoveUp (Peg (p,k) v : t) (j,l) | j==p && l==k && v==W = True
                                                |otherwise = isValidMoveRight  t (j,l)
isValidMoveDown  [] (j,t) = False
isValidMoveDown  (Peg (p,k) v : t) (j,l) | j==p && l==k && v==W = True
                                                   |otherwise = isValidMoveRight  t (j,l)

isGoal:: Board -> Bool
isGoal [] = True
isGoal (Peg (x,y) v:t) | v == B = False
                       | otherwise = isGoal t 


showPossibleNextStates:: Board -> [State]
showPossibleNextStates [] = error "No Possible States Exist."
showPossibleNextStates (Peg (x,y) v : t) | showPossibleNextStatesHH (Peg (x,y) v : t) (Peg (x,y) v : t) ==[] = error "No Possible States Exist."
                                         |otherwise = showPossibleNextStatesHH (Peg (x,y) v : t) (Peg (x,y) v : t)
showPossibleNextStatesHH [] l = []
showPossibleNextStatesHH (Peg (x,y) v : t) l| isValidMove (M (x,y)) l = (S (M (x,y)) (showPossibleNextStatesH l (M (x,y)))) : showPossibleNextStatesHH  t l
                                            | otherwise = showPossibleNextStatesHH  t l
showPossibleNextStatesH [] (M(x,y))= []										 
showPossibleNextStatesH (Peg (x,y) v : t) (M (p,l)) | x==p && y==l = Peg (x,y) W : showPossibleNextStatesH t (M (p,l))
                                                    |otherwise = Peg (x,y) v : showPossibleNextStatesH t (M (p,l))