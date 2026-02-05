package net.xuyifei.lolipickaxe.common.util.obj;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.xuyifei.lolipickaxe.LoliPickaxe;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WavefrontObject {
    private static Pattern vertexPattern = Pattern.compile("(v( (\\-){0,1}\\d+(\\.\\d+)?){3,4} *\\n)|(v( (\\-){0,1}\\d+(\\.\\d+)?){3,4} *$)");
    private static Pattern vertexNormalPattern = Pattern.compile("(vn( (\\-){0,1}\\d+(\\.\\d+)?){3,4} *\\n)|(vn( (\\-){0,1}\\d+(\\.\\d+)?){3,4} *$)");
    private static Pattern textureCoordinatePattern = Pattern.compile("(vt( (\\-){0,1}\\d+\\.\\d+){2,3} *\\n)|(vt( (\\-){0,1}\\d+(\\.\\d+)?){2,3} *$)");
    private static Pattern face_V_VT_VN_Pattern = Pattern.compile("(f( \\d+/\\d+/\\d+){3,} *\\n)|(f( \\d+/\\d+/\\d+){3,} *$)");
    private static Pattern face_V_VT_Pattern = Pattern.compile("(f( \\d+/\\d+){3,} *\\n)|(f( \\d+/\\d+){3,} *$)");
    private static Pattern face_V_VN_Pattern = Pattern.compile("(f( \\d+//\\d+){3,} *\\n)|(f( \\d+//\\d+){3,} *$)");
    private static Pattern face_V_Pattern = Pattern.compile("(f( \\d+){3,} *\\n)|(f( \\d+){3,} *$)");
    private static Pattern groupObjectPattern = Pattern.compile("([go]( [\\w\\d\\.]+) *\\n)|([go]( [\\w\\d\\.]+) *$)");
    private static Pattern rotationPattern = Pattern.compile("(rc( [\\d\\-\\.]+){3} *\\n)|(rc( [\\d\\-\\.]+){3} *$)");

    private static Matcher vertexMatcher, vertexNormalMatcher, textureCoordinateMatcher;
    private static Matcher face_V_VT_VN_Matcher, face_V_VT_Matcher, face_V_VN_Matcher, face_V_Matcher;
    private static Matcher groupObjectMatcher, rotationMatcher;

    public ArrayList<Vertex> vertices = new ArrayList<>();
    public ArrayList<Vertex> vertexNormals = new ArrayList<>();
    public ArrayList<TextureCoordinate> textureCoordinates = new ArrayList<>();
    public ArrayList<GroupObject> groupObjects = new ArrayList<>();
    public Map<String, Texture> mtl = Maps.newHashMap();
    private GroupObject currentGroupObject;
    private String fileName;
    private String pathName;

    public WavefrontObject(ResourceLocation resource) throws ModelFormatException {
        this.fileName = resource.toString();
        int index = this.fileName.lastIndexOf('/');
        if (index != -1) {
            this.pathName = this.fileName.substring(0, index + 1);
            index = this.pathName.indexOf(':');
            if (index != -1) {
                this.pathName = this.pathName.substring(index + 1);
            }
        }
        try {
            Resource res = Minecraft.getInstance().getResourceManager().getResource(resource).orElseThrow();
            loadObjModel(res.open());
        } catch (IOException e) {
            throw new ModelFormatException("IO Exception reading model format", e);
        }
    }

    public WavefrontObject(String filename, InputStream inputStream) throws ModelFormatException {
        this.fileName = filename;
        loadObjModel(inputStream);
    }

    private void loadObjModel(InputStream inputStream) throws ModelFormatException {
        BufferedReader reader = null;
        String currentLine = null;
        String currentMtl = null;
        int lineCount = 0;
        Vertex vertex;
        TextureCoordinate textureCoordinate;
        Face face;
        GroupObject group;
        try {
            reader = new BufferedReader(new InputStreamReader(inputStream));
            while ((currentLine = reader.readLine()) != null) {
                lineCount++;
                currentLine = currentLine.replaceAll("\\s+", " ").trim();
                if (currentLine.startsWith("#") || currentLine.length() == 0) {
                    continue;
                } else if (currentLine.startsWith("mtllib ")) {
                    parseMtl(currentLine, lineCount);
                } else if (currentLine.startsWith("usemtl ")) {
                    currentMtl = currentLine.substring(7);
                } else if (currentLine.startsWith("rc ")) {
                    if (currentGroupObject == null) {
                        currentGroupObject = new GroupObject("Default");
                    }
                    parseRotation(currentLine, lineCount);
                } else if (currentLine.startsWith("v ")) {
                    vertex = parseVertex(currentLine, lineCount);
                    if (vertex != null) {
                        vertices.add(vertex);
                    }
                } else if (currentLine.startsWith("vn ")) {
                    vertex = parseVertexNormal(currentLine, lineCount);
                    if (vertex != null) {
                        vertexNormals.add(vertex);
                    }
                } else if (currentLine.startsWith("vt ")) {
                    textureCoordinate = parseTextureCoordinate(currentLine, lineCount);
                    if (textureCoordinate != null) {
                        textureCoordinates.add(textureCoordinate);
                    }
                } else if (currentLine.startsWith("f ")) {
                    if (currentGroupObject == null) {
                        currentGroupObject = new GroupObject("Default");
                    }
                    face = parseFace(currentLine, lineCount);
                    if (face != null) {
                        face.usemtl = currentMtl;
                        currentGroupObject.addFace(face);
                    }
                } else if (currentLine.startsWith("g ") | currentLine.startsWith("o ")) {
                    group = parseGroupObject(currentLine, lineCount);
                    if (group != null) {
                        if (currentGroupObject != null) {
                            groupObjects.add(currentGroupObject);
                        }
                    }
                    currentGroupObject = group;
                }
            }
            if (currentGroupObject != null) {
                groupObjects.add(currentGroupObject);
            }
        } catch (IOException e) {
            throw new ModelFormatException("IO Exception reading model format", e);
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                // Ignore
            }
            try {
                inputStream.close();
            } catch (IOException e) {
                // Ignore
            }
        }
    }

    public void render(PoseStack poseStack, VertexConsumer vertexConsumer,
                       int packedLight, int packedOverlay,
                       float red, float green, float blue, float alpha) {

        for (GroupObject groupObject : groupObjects) {
            groupObject.render(poseStack, vertexConsumer, packedLight, packedOverlay,
                    red, green, blue, alpha);
        }
    }

    public void renderWithMaterials(PoseStack poseStack, MultiBufferSource buffer,
                                    RenderType renderType, int packedLight, int packedOverlay,
                                    float red, float green, float blue, float alpha) {

        for (GroupObject groupObject : groupObjects) {
            groupObject.render(poseStack, buffer, renderType, mtl,
                    packedLight, packedOverlay, red, green, blue, alpha);
        }
    }

    public void render(PoseStack poseStack, VertexConsumer vertexConsumer,
                       boolean flip, float height,
                       int packedLight, int packedOverlay,
                       float red, float green, float blue, float alpha) {

        poseStack.pushPose();

        if (flip) {
            poseStack.mulPose(Axis.XP.rotationDegrees(180));
            poseStack.translate(0, -height, 0);
        }

        render(poseStack, vertexConsumer, packedLight, packedOverlay,
                red, green, blue, alpha);

        poseStack.popPose();
    }

    public void renderOnly(PoseStack poseStack, VertexConsumer vertexConsumer,
                           String... groupNames) {

        for (GroupObject groupObject : groupObjects) {
            for (String groupName : groupNames) {
                if (groupName.equalsIgnoreCase(groupObject.name)) {
                    groupObject.render(poseStack, vertexConsumer,
                            15728880, 0, 1.0f, 1.0f, 1.0f, 1.0f);
                }
            }
        }
    }

    public void renderPart(PoseStack poseStack, VertexConsumer vertexConsumer,
                           String partName) {

        for (GroupObject groupObject : groupObjects) {
            if (partName.equalsIgnoreCase(groupObject.name)) {
                groupObject.render(poseStack, vertexConsumer,
                        15728880, 0, 1.0f, 1.0f, 1.0f, 1.0f);
            }
        }
    }

    public void renderAllExcept(PoseStack poseStack, VertexConsumer vertexConsumer,
                                String... excludedGroupNames) {

        for (GroupObject groupObject : groupObjects) {
            boolean skipPart = false;
            for (String excludedGroupName : excludedGroupNames) {
                if (excludedGroupName.equalsIgnoreCase(groupObject.name)) {
                    skipPart = true;
                    break;
                }
            }
            if (!skipPart) {
                groupObject.render(poseStack, vertexConsumer,
                        15728880, 0, 1.0f, 1.0f, 1.0f, 1.0f);
            }
        }
    }

    public void renderBatch(PoseStack poseStack, MultiBufferSource buffer,
                            RenderType renderType, int packedLight, int packedOverlay) {

        VertexConsumer vertexConsumer = buffer.getBuffer(renderType);
        render(poseStack, vertexConsumer, packedLight, packedOverlay,
                1.0f, 1.0f, 1.0f, 1.0f);
    }

    private void parseMtl(String line, int lineCount) throws ModelFormatException {
        if (mtl.isEmpty()) {
            line = line.substring(7);
            try {
                ResourceLocation mtlLocation = ResourceLocation.fromNamespaceAndPath(
                        LoliPickaxe.MODID,
                        pathName == null ? line : pathName + line);
                Resource res = Minecraft.getInstance().getResourceManager()
                        .getResource(mtlLocation).orElseThrow();

                try (BufferedReader reader = new BufferedReader(new InputStreamReader(res.open()))) {
                    String currentLine = null;
                    Texture currentTexture = null;
                    while ((currentLine = reader.readLine()) != null) {
                        currentLine = currentLine.replaceAll("\\s+", " ").trim();
                        if (currentLine.startsWith("#") || currentLine.isEmpty()) {
                            continue;
                        } else if (currentLine.startsWith("newmtl ")) {
                            if (currentTexture == null) {
                                currentTexture = new Texture(currentLine.substring(7));
                            } else {
                                throw new ModelFormatException("File Format Error: " + currentLine);
                            }
                        } else if (currentLine.startsWith("ka ")) {
                            if (currentTexture != null) {
                                String[] texargs = currentLine.substring(3).split(" ");
                                if (texargs.length != 3 && texargs.length != 4) {
                                    throw new ModelFormatException("File Format Error: " + currentLine);
                                }
                                float[] ka = new float[4];
                                ka[3] = 1.0F;
                                for (int i = 0; i < texargs.length; i++) {
                                    ka[i] = Float.parseFloat(texargs[i]);
                                }
                                currentTexture.updateMaterialProperties(ka, null, null, 32.0f);
                            } else {
                                throw new ModelFormatException("File Format Error: " + currentLine);
                            }
                        } else if (currentLine.startsWith("kd ")) {
                            if (currentTexture != null) {
                                String[] texargs = currentLine.substring(3).split(" ");
                                if (texargs.length != 3 && texargs.length != 4) {
                                    throw new ModelFormatException("File Format Error: " + currentLine);
                                }
                                float[] kd = new float[4];
                                kd[3] = 1.0F;
                                for (int i = 0; i < texargs.length; i++) {
                                    kd[i] = Float.parseFloat(texargs[i]);
                                }
                                currentTexture.updateMaterialProperties(null, kd, null, 32.0f);
                            } else {
                                throw new ModelFormatException("File Format Error: " + currentLine);
                            }
                        } else if (currentLine.startsWith("ks ")) {
                            if (currentTexture != null) {
                                String[] texargs = currentLine.substring(3).split(" ");
                                if (texargs.length != 3 && texargs.length != 4) {
                                    throw new ModelFormatException("File Format Error: " + currentLine);
                                }
                                float[] ks = new float[4];
                                ks[3] = 1.0F;
                                for (int i = 0; i < texargs.length; i++) {
                                    ks[i] = Float.parseFloat(texargs[i]);
                                }
                                currentTexture.updateMaterialProperties(null, null, ks, 32.0f);
                            } else {
                                throw new ModelFormatException("File Format Error: " + currentLine);
                            }
                        } else if (currentLine.startsWith("ns ")) {
                            if (currentTexture != null) {
                                float ns = Float.parseFloat(currentLine.substring(3)) * 128 / 1000;
                                currentTexture.updateMaterialProperties(null, null, null, ns);
                            } else {
                                throw new ModelFormatException("File Format Error: " + currentLine);
                            }
                        } else if (currentLine.startsWith("map_Kd ")) {
                            if (currentTexture != null) {
                                String texturePath = currentLine.substring(7);
                                ResourceLocation textureLocation = ResourceLocation.fromNamespaceAndPath(
                                        "lolipickaxe", pathName == null ? texturePath : pathName + texturePath);
                                currentTexture.texture = textureLocation;
                                mtl.put(currentTexture.name, currentTexture);
                                currentTexture = null;
                            } else {
                                throw new ModelFormatException("File Format Error: " + currentLine);
                            }
                        }
                    }
                }
            } catch (IOException e) {
                throw new ModelFormatException("File Not Found: " + line);
            }
        } else {
            throw new ModelFormatException("Double Mtl File: " + line);
        }
    }

    private void parseRotation(String line, int lineCount) throws ModelFormatException {
        if (isValidRotationLine(line)) {
            line = line.substring(line.indexOf(" ") + 1);
            String[] tokens = line.split(" ");
            try {
                if (tokens.length == 3) {
                    currentGroupObject.setRotationPoint(
                            Float.parseFloat(tokens[0]),
                            Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2])
                    );
                } else {
                    throw new ModelFormatException("Error parsing entry ('" + line + "'" + ", line " + lineCount + ") in file '" + fileName + "' - Incorrect format");
                }
            } catch (NumberFormatException e) {
                throw new ModelFormatException(String.format("Number formatting error at line %d", lineCount), e);
            }
        } else {
            throw new ModelFormatException("Error parsing entry ('" + line + "'" + ", line " + lineCount + ") in file '" + fileName + "' - Incorrect format");
        }
    }

    private Vertex parseVertex(String line, int lineCount) throws ModelFormatException {
        if (isValidVertexLine(line)) {
            line = line.substring(line.indexOf(" ") + 1);
            String[] tokens = line.split(" ");
            try {
                if (tokens.length == 2) {
                    return new Vertex(Float.parseFloat(tokens[0]), Float.parseFloat(tokens[1]));
                } else if (tokens.length == 3) {
                    return new Vertex(Float.parseFloat(tokens[0]), Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]));
                }
            } catch (NumberFormatException e) {
                throw new ModelFormatException(String.format("Number formatting error at line %d", lineCount), e);
            }
        } else {
            throw new ModelFormatException("Error parsing entry ('" + line + "'" + ", line " + lineCount + ") in file '" + fileName + "' - Incorrect format");
        }
        return null;
    }

    private Vertex parseVertexNormal(String line, int lineCount) throws ModelFormatException {
        if (isValidVertexNormalLine(line)) {
            line = line.substring(line.indexOf(" ") + 1);
            String[] tokens = line.split(" ");
            try {
                if (tokens.length == 3) {
                    return new Vertex(Float.parseFloat(tokens[0]), Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]));
                }
            } catch (NumberFormatException e) {
                throw new ModelFormatException(String.format("Number formatting error at line %d", lineCount), e);
            }
        } else {
            throw new ModelFormatException("Error parsing entry ('" + line + "'" + ", line " + lineCount + ") in file '" + fileName + "' - Incorrect format");
        }
        return null;
    }

    private TextureCoordinate parseTextureCoordinate(String line, int lineCount) throws ModelFormatException {
        if (isValidTextureCoordinateLine(line)) {
            line = line.substring(line.indexOf(" ") + 1);
            String[] tokens = line.split(" ");
            try {
                if (tokens.length == 2) {
                    return new TextureCoordinate(Float.parseFloat(tokens[0]), 1 - Float.parseFloat(tokens[1]));
                } else if (tokens.length == 3) {
                    return new TextureCoordinate(Float.parseFloat(tokens[0]), 1 - Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]));
                }
            } catch (NumberFormatException e) {
                throw new ModelFormatException(String.format("Number formatting error at line %d", lineCount), e);
            }
        } else {
            throw new ModelFormatException("Error parsing entry ('" + line + "'" + ", line " + lineCount + ") in file '" + fileName + "' - Incorrect format");
        }
        return null;
    }

    private Face parseFace(String line, int lineCount) throws ModelFormatException {
        if (isValidFaceLine(line)) {
            Face face = new Face();
            String trimmedLine = line.substring(line.indexOf(" ") + 1);
            String[] tokens = trimmedLine.split(" ");
            String[] subTokens = null;

            // 设置绘图模式
            if (tokens.length == 3) {
                face.glDrawingMode = com.mojang.blaze3d.vertex.VertexFormat.Mode.TRIANGLES.asGLMode;
            } else if (tokens.length == 4) {
                face.glDrawingMode = com.mojang.blaze3d.vertex.VertexFormat.Mode.QUADS.asGLMode;
            } else {
                face.glDrawingMode = com.mojang.blaze3d.vertex.VertexFormat.Mode.TRIANGLE_FAN.asGLMode;
            }

            if (isValidFace_V_VT_VN_Line(line)) {
                face.vertices = new Vertex[tokens.length];
                face.textureCoordinates = new TextureCoordinate[tokens.length];
                face.vertexNormals = new Vertex[tokens.length];
                for (int i = 0; i < tokens.length; ++i) {
                    subTokens = tokens[i].split("/");
                    face.vertices[i] = vertices.get(Integer.parseInt(subTokens[0]) - 1);
                    face.textureCoordinates[i] = textureCoordinates.get(Integer.parseInt(subTokens[1]) - 1);
                    face.vertexNormals[i] = vertexNormals.get(Integer.parseInt(subTokens[2]) - 1);
                }
                face.faceNormal = face.calculateFaceNormal();
            } else if (isValidFace_V_VT_Line(line)) {
                face.vertices = new Vertex[tokens.length];
                face.textureCoordinates = new TextureCoordinate[tokens.length];
                for (int i = 0; i < tokens.length; ++i) {
                    subTokens = tokens[i].split("/");
                    face.vertices[i] = vertices.get(Integer.parseInt(subTokens[0]) - 1);
                    face.textureCoordinates[i] = textureCoordinates.get(Integer.parseInt(subTokens[1]) - 1);
                }
                face.faceNormal = face.calculateFaceNormal();
            } else if (isValidFace_V_VN_Line(line)) {
                face.vertices = new Vertex[tokens.length];
                face.vertexNormals = new Vertex[tokens.length];
                for (int i = 0; i < tokens.length; ++i) {
                    subTokens = tokens[i].split("//");
                    face.vertices[i] = vertices.get(Integer.parseInt(subTokens[0]) - 1);
                    face.vertexNormals[i] = vertexNormals.get(Integer.parseInt(subTokens[1]) - 1);
                }
                face.faceNormal = face.calculateFaceNormal();
            } else if (isValidFace_V_Line(line)) {
                face.vertices = new Vertex[tokens.length];
                for (int i = 0; i < tokens.length; ++i) {
                    face.vertices[i] = vertices.get(Integer.parseInt(tokens[i]) - 1);
                }
                face.faceNormal = face.calculateFaceNormal();
            } else {
                throw new ModelFormatException("Error parsing entry ('" + line + "'" + ", line " + lineCount + ") in file '" + fileName + "' - Incorrect format");
            }
            return face;
        } else {
            throw new ModelFormatException("Error parsing entry ('" + line + "'" + ", line " + lineCount + ") in file '" + fileName + "' - Incorrect format");
        }
    }

    private GroupObject parseGroupObject(String line, int lineCount) throws ModelFormatException {
        if (isValidGroupObjectLine(line)) {
            String trimmedLine = line.substring(line.indexOf(" ") + 1);
            if (!trimmedLine.isEmpty()) {
                return new GroupObject(trimmedLine);
            }
        } else {
            throw new ModelFormatException("Error parsing entry ('" + line + "'" + ", line " + lineCount + ") in file '" + fileName + "' - Incorrect format");
        }
        return null;
    }

    private static boolean isValidVertexLine(String line) {
        if (vertexMatcher != null) {
            vertexMatcher.reset();
        }
        vertexMatcher = vertexPattern.matcher(line);
        return vertexMatcher.matches();
    }

    private static boolean isValidVertexNormalLine(String line) {
        if (vertexNormalMatcher != null) {
            vertexNormalMatcher.reset();
        }
        vertexNormalMatcher = vertexNormalPattern.matcher(line);
        return vertexNormalMatcher.matches();
    }

    private static boolean isValidTextureCoordinateLine(String line) {
        if (textureCoordinateMatcher != null) {
            textureCoordinateMatcher.reset();
        }
        textureCoordinateMatcher = textureCoordinatePattern.matcher(line);
        return textureCoordinateMatcher.matches();
    }

    private static boolean isValidFace_V_VT_VN_Line(String line) {
        if (face_V_VT_VN_Matcher != null) {
            face_V_VT_VN_Matcher.reset();
        }
        face_V_VT_VN_Matcher = face_V_VT_VN_Pattern.matcher(line);
        return face_V_VT_VN_Matcher.matches();
    }

    private static boolean isValidFace_V_VT_Line(String line) {
        if (face_V_VT_Matcher != null) {
            face_V_VT_Matcher.reset();
        }
        face_V_VT_Matcher = face_V_VT_Pattern.matcher(line);
        return face_V_VT_Matcher.matches();
    }

    private static boolean isValidFace_V_VN_Line(String line) {
        if (face_V_VN_Matcher != null) {
            face_V_VN_Matcher.reset();
        }
        face_V_VN_Matcher = face_V_VN_Pattern.matcher(line);
        return face_V_VN_Matcher.matches();
    }

    private static boolean isValidFace_V_Line(String line) {
        if (face_V_Matcher != null) {
            face_V_Matcher.reset();
        }
        face_V_Matcher = face_V_Pattern.matcher(line);
        return face_V_Matcher.matches();
    }

    private static boolean isValidFaceLine(String line) {
        return isValidFace_V_VT_VN_Line(line) || isValidFace_V_VT_Line(line) || isValidFace_V_VN_Line(line) || isValidFace_V_Line(line);
    }

    private static boolean isValidGroupObjectLine(String line) {
        if (groupObjectMatcher != null) {
            groupObjectMatcher.reset();
        }
        groupObjectMatcher = groupObjectPattern.matcher(line);
        return groupObjectMatcher.matches();
    }

    private static boolean isValidRotationLine(String line) {
        if (rotationMatcher != null) {
            rotationMatcher.reset();
        }
        rotationMatcher = rotationPattern.matcher(line);
        return rotationMatcher.matches();
    }
}
