#!/usr/bin/env bash
#Check the docker running process. It will only work if you know the docker container name.
#current_time=$(date "+%Y%m%d-%H%M%S")
#/dev/null 2>&1 &&
file_name=initail_running_containers.txt
function dockerContainerCheck() {
  #first running status
  if [ ! -e $file_name ]; then
    # get running containers Id
     docker ps --format '{{.ID}}'| awk '{print $1}' > $file_name
  fi

  while read line
  do
    STR1=$line
    echo "First file :: $STR1"
    # get the exited containers Id
    docker ps --format '{{.ID}}' -f status=exited | awk '{print $1}' > temp.txt
    # shellcheck disable=SC2162
    while read tmpLine
    do
      STR2=$tmpLine
      echo "Temp File :: $STR2"
      if [[ "$STR1" != "$STR2" ]]; then
          # start the initial running containers
          docker start "$STR1"
          sleep 15
      fi
    done < temp.txt
    rm temp.txt # delete the file
  done < $file_name
#  container_status=( $(docker ps -a --format "{{.Status}}") )
#  if [ ${#containers[@]} == 0 ]; then #cehck the length to ensure there is no containers running.
#      container_names=( $(docker ps --format "{{.Names}}") )
#
#  fi
}
echo "Start of script..."
dockerContainerCheck;
echo "End of script..."