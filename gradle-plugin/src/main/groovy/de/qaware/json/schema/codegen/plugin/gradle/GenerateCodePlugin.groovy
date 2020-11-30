package de.qaware.json.schema.codegen.plugin.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project

class GenerateCodePlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.task('generate-code', type: GenerateCodeTask)
    }

}
