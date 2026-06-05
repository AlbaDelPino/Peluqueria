package com.example.demo.controller;

import com.example.demo.domain.*;
import com.example.demo.repository.*;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.InputStream;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;

@RestController
@RequestMapping("/api/import")
@CrossOrigin(origins = "*")
public class ExcelImportController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ServicioRepository servicioRepository;

    @Autowired
    private TipoServicioRepository tipoServicioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private HorarioRepository horarioRepository;

    @Autowired
    private GrupoRepository grupoRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @PostMapping("/clientes")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> importClientes(@RequestParam("file") MultipartFile file) {
        Map<String, Object> result = new HashMap<>();
        List<String> errors = new ArrayList<>();
        int totalRows = 0;
        int importedCount = 0;
        int updatedCount = 0;
        int failedCount = 0;

        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "El archivo está vacío"));
            }

            try (InputStream is = file.getInputStream();
                 Workbook workbook = WorkbookFactory.create(is)) {

                Sheet sheet = workbook.getSheetAt(0);
                if (sheet.getPhysicalNumberOfRows() <= 0) {
                    return ResponseEntity.badRequest().body(Map.of("error", "La hoja de Excel no tiene filas"));
                }

                Row headerRow = sheet.getRow(0);
                if (headerRow == null) {
                    return ResponseEntity.badRequest().body(Map.of("error", "No se encontró fila de cabecera"));
                }

                // Detect column indexes
                int colNombre = findColumnIndex(headerRow, "nombre", "completo", "name", "cliente", "client");
                int colEmail = findColumnIndex(headerRow, "email", "correo", "mail", "e-mail");
                int colTelefono = findColumnIndex(headerRow, "telefono", "teléfono", "phone", "celular", "contacto");
                int colObservacion = findColumnIndex(headerRow, "observacion", "observaciones", "notes", "nota", "comentario");
                int colAlergenos = findColumnIndex(headerRow, "alergenos", "alergias", "allergens", "alergia");
                int colUsername = findColumnIndex(headerRow, "username", "usuario", "user");
                int colPassword = findColumnIndex(headerRow, "password", "contrasenya", "contraseña", "clave", "pass");

                // Check minimum requirements
                if (colNombre == -1) {
                    return ResponseEntity.badRequest().body(Map.of("error", "La columna 'Nombre' es obligatoria en las cabeceras"));
                }

                int lastRowNum = sheet.getLastRowNum();
                for (int i = 1; i <= lastRowNum; i++) {
                    Row row = sheet.getRow(i);
                    if (row == null || isRowEmpty(row)) {
                        continue;
                    }

                    totalRows++;
                    try {
                        String nombre = getCellStringValue(row.getCell(colNombre));
                        if (nombre == null || nombre.isBlank()) {
                            throw new IllegalArgumentException("El nombre es obligatorio");
                        }

                        String email = colEmail != -1 ? getCellStringValue(row.getCell(colEmail)) : null;
                        Long telefono = colTelefono != -1 ? getCellLongValue(row.getCell(colTelefono)) : null;
                        String observacion = colObservacion != -1 ? getCellStringValue(row.getCell(colObservacion)) : "";
                        String alergenos = colAlergenos != -1 ? getCellStringValue(row.getCell(colAlergenos)) : "";
                        String username = colUsername != -1 ? getCellStringValue(row.getCell(colUsername)) : null;
                        String contrasenya = colPassword != -1 ? getCellStringValue(row.getCell(colPassword)) : null;

                        // Check if client exists (by email first, or username if email is empty)
                        Optional<User> existingUserOpt = Optional.empty();
                        if (email != null && !email.isBlank()) {
                            existingUserOpt = userRepository.findByEmail(email);
                        } else if (username != null && !username.isBlank()) {
                            existingUserOpt = userRepository.findByUsername(username);
                        }

                        if (existingUserOpt.isPresent()) {
                            User user = existingUserOpt.get();
                            if (user.getRole() != ERole.ROLE_CLIENTE) {
                                throw new IllegalArgumentException("El usuario con email/usuario '" + (email != null ? email : username) + "' no es un cliente (es " + user.getRole() + ")");
                            }

                            Cliente cliente = (Cliente) user;
                            cliente.setNombre(nombre);
                            if (telefono != null) {
                                cliente.setTelefono(telefono);
                            }
                            if (observacion != null) cliente.setObservacion(observacion);
                            if (alergenos != null) cliente.setAlergenos(alergenos);
                            if (username != null && !username.isBlank()) cliente.setUsername(username);
                            if (contrasenya != null && !contrasenya.isBlank()) {
                                cliente.setContrasenya(passwordEncoder.encode(contrasenya));
                            }

                            clienteRepository.save(cliente);
                            updatedCount++;
                        } else {
                            Cliente cliente = new Cliente();
                            cliente.setRole(ERole.ROLE_CLIENTE);
                            cliente.setNombre(nombre);
                            cliente.setEmail(email);
                            cliente.setTelefono(telefono != null ? telefono : 0L);
                            cliente.setObservacion(observacion);
                            cliente.setAlergenos(alergenos);

                            // Left empty if not in Excel as per instruction
                            if (username == null || username.isBlank()) {
                                throw new IllegalArgumentException("El nombre de usuario es obligatorio para nuevos registros y no se especificó");
                            }
                            if (contrasenya == null || contrasenya.isBlank()) {
                                throw new IllegalArgumentException("La contraseña es obligatoria para nuevos registros y no se especificó");
                            }

                            cliente.setUsername(username);
                            cliente.setContrasenya(passwordEncoder.encode(contrasenya));

                            clienteRepository.save(cliente);
                            importedCount++;
                        }
                    } catch (Exception e) {
                        failedCount++;
                        errors.add("Fila " + (i + 1) + ": " + e.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Error procesando el archivo de Excel: " + e.getMessage()));
        }

        result.put("totalRows", totalRows);
        result.put("imported", importedCount);
        result.put("updated", updatedCount);
        result.put("failed", failedCount);
        result.put("errors", errors);

        return ResponseEntity.ok(result);
    }

    @PostMapping("/servicios")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> importServicios(@RequestParam("file") MultipartFile file) {
        Map<String, Object> result = new HashMap<>();
        List<String> errors = new ArrayList<>();
        int totalRows = 0;
        int importedCount = 0;
        int updatedCount = 0;
        int failedCount = 0;

        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "El archivo está vacío"));
            }

            try (InputStream is = file.getInputStream();
                 Workbook workbook = WorkbookFactory.create(is)) {

                Sheet sheet = workbook.getSheetAt(0);
                if (sheet.getPhysicalNumberOfRows() <= 0) {
                    return ResponseEntity.badRequest().body(Map.of("error", "La hoja de Excel no tiene filas"));
                }

                Row headerRow = sheet.getRow(0);
                if (headerRow == null) {
                    return ResponseEntity.badRequest().body(Map.of("error", "No se encontró fila de cabecera"));
                }

                // Detect column indexes
                int colNombre = findColumnIndex(headerRow, "nombre", "name", "servicio", "service");
                int colDescripcion = findColumnIndex(headerRow, "descripcion", "descripción", "description", "detalle");
                int colPrecio = findColumnIndex(headerRow, "precio", "price", "costo", "coste");
                int colDuracion = findColumnIndex(headerRow, "duracion", "duración", "duration", "tiempo", "minutos");
                int colTipo = findColumnIndex(headerRow, "tipo", "categoria", "categoría", "category");

                if (colNombre == -1) {
                    return ResponseEntity.badRequest().body(Map.of("error", "La columna 'Nombre' es obligatoria en las cabeceras"));
                }
                if (colTipo == -1) {
                    return ResponseEntity.badRequest().body(Map.of("error", "La columna 'Tipo/Categoría' es obligatoria en las cabeceras"));
                }

                int lastRowNum = sheet.getLastRowNum();
                for (int i = 1; i <= lastRowNum; i++) {
                    Row row = sheet.getRow(i);
                    if (row == null || isRowEmpty(row)) {
                        continue;
                    }

                    totalRows++;
                    try {
                        String nombre = getCellStringValue(row.getCell(colNombre));
                        if (nombre == null || nombre.isBlank()) {
                            throw new IllegalArgumentException("El nombre del servicio es obligatorio");
                        }

                        String descripcion = colDescripcion != -1 ? getCellStringValue(row.getCell(colDescripcion)) : "";
                        Long precio = colPrecio != -1 ? getCellLongValue(row.getCell(colPrecio)) : 0L;
                        Long duracion = colDuracion != -1 ? getCellLongValue(row.getCell(colDuracion)) : 0L;
                        String tipoNombre = getCellStringValue(row.getCell(colTipo));

                        if (tipoNombre == null || tipoNombre.isBlank()) {
                            throw new IllegalArgumentException("La categoría/tipo de servicio es obligatoria");
                        }

                        TipoServicio tipoServicio = getOrCreateTipoServicio(tipoNombre);

                        // Check if service already exists
                        Set<Servicio> existingServices = servicioRepository.findByNombre(nombre);
                        if (existingServices != null && !existingServices.isEmpty()) {
                            // Update the first matching service
                            Servicio servicio = existingServices.iterator().next();
                            if (descripcion != null) servicio.setDescripcion(descripcion);
                            if (precio != null) servicio.setPrecio(precio);
                            if (duracion != null) servicio.setDuracion(duracion);
                            servicio.setTipoServicio(tipoServicio);

                            servicioRepository.save(servicio);
                            updatedCount++;
                        } else {
                            // Create new service
                            Servicio servicio = new Servicio();
                            servicio.setNombre(nombre);
                            servicio.setDescripcion(descripcion);
                            servicio.setPrecio(precio);
                            servicio.setDuracion(duracion);
                            servicio.setTipoServicio(tipoServicio);

                            servicioRepository.save(servicio);
                            importedCount++;
                        }
                    } catch (Exception e) {
                        failedCount++;
                        errors.add("Fila " + (i + 1) + ": " + e.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Error procesando el archivo de Excel: " + e.getMessage()));
        }

        result.put("totalRows", totalRows);
        result.put("imported", importedCount);
        result.put("updated", updatedCount);
        result.put("failed", failedCount);
        result.put("errors", errors);

        return ResponseEntity.ok(result);
    }

    private int findColumnIndex(Row headerRow, String... keywords) {
        for (Cell cell : headerRow) {
            if (cell.getCellType() == CellType.STRING) {
                String val = cell.getStringCellValue().trim().toLowerCase();
                for (String kw : keywords) {
                    if (val.contains(kw)) {
                        return cell.getColumnIndex();
                    }
                }
            }
        }
        return -1;
    }

    private boolean isRowEmpty(Row row) {
        for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
            Cell cell = row.getCell(c);
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                return false;
            }
        }
        return true;
    }

    private String getCellStringValue(Cell cell) {
        if (cell == null) return null;
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                double numericVal = cell.getNumericCellValue();
                if (numericVal == (long) numericVal) {
                    return String.valueOf((long) numericVal);
                }
                return String.valueOf(numericVal);
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return "";
        }
    }

    private Long getCellLongValue(Cell cell) {
        if (cell == null) return null;
        switch (cell.getCellType()) {
            case NUMERIC:
                return (long) cell.getNumericCellValue();
            case STRING:
                String val = cell.getStringCellValue().trim();
                if (val.isEmpty()) return null;
                try {
                    // Handle decimals parsed as long
                    if (val.contains(".")) {
                        return (long) Double.parseDouble(val);
                    }
                    return Long.parseLong(val);
                } catch (NumberFormatException e) {
                    return null;
                }
            default:
                return null;
        }
    }

    private TipoServicio getOrCreateTipoServicio(String nombre) {
        Optional<TipoServicio> opt = tipoServicioRepository.findByNombre(nombre);
        if (opt.isPresent()) {
            return opt.get();
        } else {
            TipoServicio ts = new TipoServicio(nombre);
            return tipoServicioRepository.save(ts);
        }
    }

    @PostMapping("/horarios")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> importHorarios(@RequestParam("file") MultipartFile file) {
        Map<String, Object> result = new HashMap<>();
        List<String> errors = new ArrayList<>();
        int totalRows = 0;
        int importedCount = 0;
        int updatedCount = 0;
        int failedCount = 0;

        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "El archivo está vacío"));
            }

            try (InputStream is = file.getInputStream();
                 Workbook workbook = WorkbookFactory.create(is)) {

                Sheet sheet = workbook.getSheetAt(0);
                if (sheet.getPhysicalNumberOfRows() <= 0) {
                    return ResponseEntity.badRequest().body(Map.of("error", "La hoja de Excel no tiene filas"));
                }

                Row headerRow = sheet.getRow(0);
                if (headerRow == null) {
                    return ResponseEntity.badRequest().body(Map.of("error", "No se encontró fila de cabecera"));
                }

                // Detect column indexes
                int colDia = findColumnIndex(headerRow, "dia", "día", "day", "semana");
                int colInicio = findColumnIndex(headerRow, "inicio", "hora_inicio", "hora inicio", "start", "desde");
                int colFin = findColumnIndex(headerRow, "fin", "hora_fin", "hora fin", "end", "hasta");
                int colPlazas = findColumnIndex(headerRow, "plazas", "capacidad", "slots", "cupos");
                int colServicio = findColumnIndex(headerRow, "servicio", "id_servicio", "service");
                int colGrupo = findColumnIndex(headerRow, "grupo", "id_grupo", "group");
                int colCurso = findColumnIndex(headerRow, "curso", "id_curso", "year", "academic");

                if (colDia == -1) {
                    return ResponseEntity.badRequest().body(Map.of("error", "La columna 'Día' es obligatoria"));
                }
                if (colInicio == -1) {
                    return ResponseEntity.badRequest().body(Map.of("error", "La columna 'Hora Inicio' es obligatoria"));
                }
                if (colFin == -1) {
                    return ResponseEntity.badRequest().body(Map.of("error", "La columna 'Hora Fin' es obligatoria"));
                }
                if (colServicio == -1) {
                    return ResponseEntity.badRequest().body(Map.of("error", "La columna 'Servicio' es obligatoria"));
                }
                if (colGrupo == -1) {
                    return ResponseEntity.badRequest().body(Map.of("error", "La columna 'Grupo' es obligatoria"));
                }

                int lastRowNum = sheet.getLastRowNum();
                for (int i = 1; i <= lastRowNum; i++) {
                    Row row = sheet.getRow(i);
                    if (row == null || isRowEmpty(row)) {
                        continue;
                    }

                    totalRows++;
                    try {
                        String diaSemana = getCellStringValue(row.getCell(colDia));
                        if (diaSemana == null || diaSemana.isBlank()) {
                            throw new IllegalArgumentException("El día de la semana es obligatorio");
                        }
                        diaSemana = diaSemana.trim().toUpperCase();

                        LocalTime horaInicio = getCellLocalTimeValue(row.getCell(colInicio));
                        if (horaInicio == null) {
                            throw new IllegalArgumentException("La hora de inicio es obligatoria o inválida");
                        }

                        LocalTime horaFin = getCellLocalTimeValue(row.getCell(colFin));
                        if (horaFin == null) {
                            throw new IllegalArgumentException("La hora de fin es obligatoria o inválida");
                        }

                        if (!horaFin.isAfter(horaInicio)) {
                            throw new IllegalArgumentException("La hora de fin debe ser posterior a la hora de inicio");
                        }

                        Long plazas = colPlazas != -1 ? getCellLongValue(row.getCell(colPlazas)) : 1L;
                        if (plazas == null || plazas < 0) {
                            plazas = 1L;
                        }

                        String servicioIdent = getCellStringValue(row.getCell(colServicio));
                        Servicio servicio = findServicio(servicioIdent);
                        if (servicio == null) {
                            throw new IllegalArgumentException("Servicio no encontrado: " + servicioIdent);
                        }

                        String grupoIdent = getCellStringValue(row.getCell(colGrupo));
                        Grupo grupo = findGrupo(grupoIdent);
                        if (grupo == null) {
                            throw new IllegalArgumentException("Grupo no encontrado: " + grupoIdent);
                        }

                        // Get CursoEscolar
                        CursoEscolar curso = null;
                        if (colCurso != -1) {
                            String cursoNombre = getCellStringValue(row.getCell(colCurso));
                            if (cursoNombre != null && !cursoNombre.isBlank()) {
                                curso = cursoRepository.findByNombre(cursoNombre).orElse(null);
                                if (curso == null) {
                                    // Create new CursoEscolar if not found
                                    curso = new CursoEscolar(cursoNombre);
                                    curso = cursoRepository.save(curso);
                                }
                            }
                        }

                        if (curso == null) {
                            curso = cursoRepository.findBySeleccionado(true);
                        }

                        if (curso == null) {
                            // Find any course or create a default one
                            List<CursoEscolar> allCursos = cursoRepository.findAll();
                            if (!allCursos.isEmpty()) {
                                curso = allCursos.get(0);
                            } else {
                                curso = new CursoEscolar("Curso General");
                                curso.setSeleccionado(true);
                                curso = cursoRepository.save(curso);
                            }
                        }

                        // Validate duration
                        long durationMinutes = horaInicio.until(horaFin, ChronoUnit.MINUTES);
                        if (servicio.getDuracion() > durationMinutes) {
                            throw new IllegalArgumentException("El horario no es lo suficientemente largo para ofrecer este servicio (Duración servicio: " + servicio.getDuracion() + " min, duración slot: " + durationMinutes + " min)");
                        }

                        // Check if HorarioSemanal already exists
                        List<HorarioSemanal> existing = horarioRepository.findByServicioAndHoraInicioAndDiaSemanaAndGrupoAndCurso(
                                servicio, horaInicio, diaSemana, grupo, curso
                        );

                        if (existing != null && !existing.isEmpty()) {
                            HorarioSemanal horario = existing.get(0);
                            horario.setHoraFin(horaFin);
                            horario.setPlazas(plazas);
                            horarioRepository.save(horario);
                            updatedCount++;
                        } else {
                            HorarioSemanal horario = new HorarioSemanal();
                            horario.setDiaSemana(diaSemana);
                            horario.setHoraInicio(horaInicio);
                            horario.setHoraFin(horaFin);
                            horario.setPlazas(plazas);
                            horario.setServicio(servicio);
                            horario.setGrupo(grupo);
                            horario.setCurso(curso);
                            horarioRepository.save(horario);
                            importedCount++;
                        }

                    } catch (Exception e) {
                        failedCount++;
                        errors.add("Fila " + (i + 1) + ": " + e.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Error procesando el archivo de Excel: " + e.getMessage()));
        }

        result.put("totalRows", totalRows);
        result.put("imported", importedCount);
        result.put("updated", updatedCount);
        result.put("failed", failedCount);
        result.put("errors", errors);

        return ResponseEntity.ok(result);
    }

    private LocalTime getCellLocalTimeValue(Cell cell) {
        if (cell == null) return null;
        if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
            Date date = cell.getDateCellValue();
            return date.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
        }
        String str = getCellStringValue(cell);
        if (str == null || str.isBlank()) return null;
        try {
            return LocalTime.parse(str);
        } catch (Exception e) {
            try {
                if (str.length() == 4) {
                    return LocalTime.parse("0" + str);
                }
                double val = Double.parseDouble(str);
                int totalSeconds = (int) (val * 86400);
                int hours = totalSeconds / 3600;
                int minutes = (totalSeconds % 3600) / 60;
                int seconds = totalSeconds % 60;
                return LocalTime.of(hours, minutes, seconds);
            } catch (Exception ex) {
                return null;
            }
        }
    }

    private Grupo findGrupo(String identifier) {
        if (identifier == null || identifier.isBlank()) return null;
        Optional<User> opt = userRepository.findByUsername(identifier);
        if (opt.isPresent() && opt.get() instanceof Grupo) {
            return (Grupo) opt.get();
        }
        opt = userRepository.findByEmail(identifier);
        if (opt.isPresent() && opt.get() instanceof Grupo) {
            return (Grupo) opt.get();
        }
        List<User> groups = userRepository.findByRole(ERole.ROLE_GRUPO);
        for (User u : groups) {
            if (u instanceof Grupo && (u.getNombre().equalsIgnoreCase(identifier) || u.getUsername().equalsIgnoreCase(identifier))) {
                return (Grupo) u;
            }
        }
        return null;
    }

    private Servicio findServicio(String identifier) {
        if (identifier == null || identifier.isBlank()) return null;
        Set<Servicio> existingServices = servicioRepository.findByNombre(identifier);
        if (existingServices != null && !existingServices.isEmpty()) {
            return existingServices.iterator().next();
        }
        try {
            long id = Long.parseLong(identifier);
            return servicioRepository.findById(id).orElse(null);
        } catch (NumberFormatException e) {
            // ignore
        }
        return null;
    }
}
