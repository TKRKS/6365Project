import sys

def main():
    if (len(sys.argv) < 4):
        print "usage: python resize.py [inputfile] [outputfile] [max_connection_count] [optional:min_connection_count]"
        return
    inf = open(sys.argv[1], "r")
    outf = open(sys.argv[2], "w")
    maxcount = int(sys.argv[3])
    count, total = 0, 0
    for line in inf.readlines():
        total += 1
        if (len(line.split()) <= maxcount + 1):
            if ((len(sys.argv)) > 4 and (len(line.split()) > int(sys.argv[4]))):
                outf.write("%s\n"%(line.strip()))
                count += 1
    print "finished filtering! written %i lines out of %i" %(count, total)



if (__name__ == "__main__"):
    main()
