for i in {1..10000}
do
    echo "$i 1000000" | ./wojtek.exe > wojtek.out
    echo "$i 1000000" | ./karol.exe > karol.out
    diff -bw wojtek.out karol.out
    if [ $? -eq 0 ]
    then
        echo $i OK
    else
        echo $i BLAD
    fi
done