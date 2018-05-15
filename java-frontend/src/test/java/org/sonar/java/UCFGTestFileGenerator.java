/*
 * SonarQube Java
 * Copyright (C) 2012-2018 SonarSource SA
 * mailto:info AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.java;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.stream.Collectors;
import org.mockito.Mockito;
import org.sonar.java.ast.parser.JavaParser;
import org.sonar.java.bytecode.loader.SquidClassLoader;
import org.sonar.java.resolve.SemanticModel;
import org.sonar.plugins.java.api.JavaFileScannerContext;
import org.sonar.plugins.java.api.tree.CompilationUnitTree;

public class UCFGTestFileGenerator {

  public static void main(String[] args) throws IOException {
    File testJarsDir = new File("target/test-jars/");
    SquidClassLoader squidClassLoader = new SquidClassLoader(Arrays.asList(testJarsDir.listFiles()));

    File root = new File("../../../../sonar-security/sonar-security-plugin/src/test/resources");
    String filename = "S2076.java";
    File file = new File(root, filename);

    UCFGJavaVisitor generator = new UCFGJavaVisitor(root);

    String fileContent = Files.lines(file.toPath()).collect(Collectors.joining("\n"));

    CompilationUnitTree cut = (CompilationUnitTree) JavaParser.createParser().parse(fileContent);
    SemanticModel semanticModel = SemanticModel.createFor(cut, squidClassLoader);

    JavaFileScannerContext context = Mockito.mock(JavaFileScannerContext.class);
    Mockito.when(context.getFileKey()).thenReturn(filename);
    Mockito.when(context.getTree()).thenReturn(cut);
    Mockito.when(context.getSemanticModel()).thenReturn(semanticModel);

    generator.scanFile(context);
  }
}
