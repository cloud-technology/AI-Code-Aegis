package io.github.cloudtechnology.codeaegis;

import java.util.List;
import java.util.Set;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import io.github.cloudtechnology.codeaegis.utility.DirectoryExplorer;
import io.github.cloudtechnology.codeaegis.utility.FileInfo;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
// @RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class ApplicationStartup {

  @EventListener(ApplicationReadyEvent.class)
  public void afterStartup() throws Exception {
    

  }

}
