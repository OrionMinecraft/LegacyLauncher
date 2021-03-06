/*
 * This file is part of project Orion, licensed under the MIT License (MIT).
 *
 * Copyright (c) Original contributors ("I don't care" license? See https://github.com/Mojang/LegacyLauncher/issues/1)
 * Copyright (c) 2017-2018 Mark Vainomaa <mikroskeem@mikroskeem.eu>
 * Copyright (c) Contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package eu.mikroskeem.test.launchwrapper;

import net.minecraft.launchwrapper.IClassTransformer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;

import java.util.List;

import static eu.mikroskeem.shuriken.instrumentation.bytecode.ClassManipulation.findField;
import static eu.mikroskeem.shuriken.instrumentation.bytecode.ClassManipulation.readClass;

/**
 * @author Mark Vainomaa
 */
public final class TestTransformer implements IClassTransformer {
    @Nullable
    @Override
    public byte[] transform(@NotNull String name, @NotNull String transformedName, @Nullable byte[] classData) {
        if(classData == null) return null;

        if(!name.equals("eu.mikroskeem.test.launchwrapper.targets.TransformableClass")) {
            return classData;
        }

        ClassWriter cw = new ClassWriter(0);
        ClassNode cn = readClass(classData);

        @SuppressWarnings("unchecked")
        List<FieldNode> fields = (List<FieldNode>) cn.fields;

        FieldNode field = findField(fields, Opcodes.ACC_PRIVATE | Opcodes.ACC_FINAL, "a", "Ljava/lang/String;");
        field.access = Opcodes.ACC_PUBLIC;

        cn.accept(cw);
        return cw.toByteArray();
    }
}
